package net.frey.spring6webmvc.service;

import com.opencsv.bean.CsvToBeanBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import net.frey.spring6webmvc.model.BeerCsv;
import org.springframework.stereotype.Service;

@Service
public class BeerCsvServiceImpl implements BeerCsvService {
    @Override
    public List<BeerCsv> convertCsv(File csvFile) {
        try {
            return new CsvToBeanBuilder<BeerCsv>(new FileReader(csvFile))
                    .withType(BeerCsv.class)
                    .build()
                    .parse();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
