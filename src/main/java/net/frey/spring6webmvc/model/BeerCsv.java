package net.frey.spring6webmvc.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeerCsv {
    @CsvBindByName
    private int row;

    @CsvBindByName(column = "count.x")
    private int count_x;

    @CsvBindByName
    private String abv;

    @CsvBindByName
    private String ibu;

    @CsvBindByName
    private int id;

    @CsvBindByName
    private String beer;

    @CsvBindByName
    private String style;

    @CsvBindByName(column = "brewer_id")
    private int breweryId;

    @CsvBindByName
    private float ounces;

    @CsvBindByName
    private String style2;

    @CsvBindByName(column = "count.y")
    private String count_y;

    @CsvBindByName
    private String city;

    @CsvBindByName
    private String state;

    @CsvBindByName
    private String label;
}
