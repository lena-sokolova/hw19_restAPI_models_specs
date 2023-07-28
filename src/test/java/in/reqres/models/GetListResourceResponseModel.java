package in.reqres.models;

import lombok.Data;

import java.util.List;

@Data
public class GetListResourceResponseModel {
    Integer page, per_page, total, total_pages;
    List<Data> data;
    Support support;

    @lombok.Data
    public static class Data {
        Integer id;
        String name, year, color, pantone_value;
    }

    @lombok.Data
    public static class Support {
        String url, text;
    }
}