package net.frey.spring6webmvc.service;

import java.io.File;
import java.util.List;
import net.frey.spring6webmvc.model.BeerCsv;

public interface BeerCsvService {
    List<BeerCsv> convertCsv(File csvFile);
}
