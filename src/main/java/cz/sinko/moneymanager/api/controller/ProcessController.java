package cz.sinko.moneymanager.api.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cz.sinko.moneymanager.config.exception.ResourceNotFoundException;
import cz.sinko.moneymanager.facade.ImportFacade;
import cz.sinko.moneymanager.facade.RuleFacade;
import cz.sinko.moneymanager.repository.model.Transaction;
import cz.sinko.moneymanager.service.CsvUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Controller for processing csv bank statements.
 *
 * @author Sinko Radovan
 */
@RestController
@RequestMapping("/process")
@RequiredArgsConstructor
@Slf4j
public class ProcessController {

    private final ImportFacade importFacade;

    private final RuleFacade ruleFacade;

    @PostMapping(consumes = "multipart/form-data", produces = "text/csv")
    public ResponseEntity<?> processBankStatement(@RequestParam(value = "csob-debit", required = false) MultipartFile csobDebitFile,
                                                  @RequestParam(value = "csob-kredit", required = false) MultipartFile csobKreditFile,
                                                  @RequestParam(value = "kb", required = false) MultipartFile kbFile,
                                                  @RequestParam(value = "unicredit", required = false) MultipartFile unicreditFile,
                                                  @RequestParam(value = "airbank", required = false) MultipartFile airbankFile,
                                                  @RequestParam("filename") String filename) throws ResourceNotFoundException {

        if (csobDebitFile == null && csobKreditFile == null && kbFile == null && unicreditFile == null && airbankFile == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file was provided!");
        }

        List<Transaction> csobDebitTransactions = null;
        if (csobDebitFile != null) {
            log.info("Processing ČSOB debit file: " + csobDebitFile.getOriginalFilename());
            csobDebitTransactions = importFacade.csobParseTransactions(csobDebitFile);
        }

        List<Transaction> csobKreditTransactions = null;
        if (csobKreditFile != null) {
            log.info("Processing ČSOB kredit file: " + csobKreditFile.getOriginalFilename());
            csobKreditTransactions = importFacade.csobKreditParseTransactions(csobKreditFile);
        }

        List<Transaction> kbTransactions = null;
        if (kbFile != null) {
            log.info("Processing KB file: " + kbFile.getOriginalFilename());
//                        kbTransactions = importFacade.kbParseTransactions(kbFile);
        }

        List<Transaction> unicreditTransactions = null;
        if (unicreditFile != null) {
            log.info("Processing Unicredit file: " + unicreditFile.getOriginalFilename());
//                        unicreditTransactions = importFacade.unicreditParseTransactions(unicreditFile);
        }

        List<Transaction> airbankTransactions = null;
        if (airbankFile != null) {
            log.info("Processing AirBank file: " + airbankFile.getOriginalFilename());
//                        airbankTransactions = importFacade.airbankParseTransactions(airbankFile);
        }

        final List<Transaction> transactions =
            Stream.of(csobDebitTransactions, csobKreditTransactions, kbTransactions, unicreditTransactions, airbankTransactions)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .sorted(Comparator.comparing(Transaction::getDate))
                .toList();

        return processCsvFile(transactions, filename);
    }

    private ResponseEntity<?> processCsvFile(List<Transaction> transactions, String csvFilename) {
        final List<Transaction> processedTransactions = ruleFacade.applyRules(transactions);
        return createCsvFile(processedTransactions, csvFilename);
    }

    private ResponseEntity<?> createCsvFile(List<Transaction> transactions, String csvFilename) {
        final InputStreamResource output = CsvUtil.createCsvOutput(transactions);
        return ResponseEntity
            .status(HttpStatus.OK)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + csvFilename)
            .header(HttpHeaders.CONTENT_TYPE, "text/csv")
            .body(output);
    }
}