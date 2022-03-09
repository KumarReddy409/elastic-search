package com.test.elasticsearch.mapper;

import com.test.elasticsearch.dto.SearchParameters;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryRowMapper implements RowMapper<SearchParameters> {

    @Override
    public SearchParameters mapRow(ResultSet rs, int rowNum) throws SQLException {
        return SearchParameters.builder()
                .locationId(rs.getString("locationId"))
                .itemId(rs.getString("itemId"))
                .locationName(rs.getString("locationName"))
                .city(rs.getString("city"))
                .sellPrice(rs.getString("sell_price"))
                .itemName(rs.getString("name"))
                .itemImageId(rs.getString("imageId"))
                .itemImageType(rs.getString("mime_type"))
                .tagId(rs.getString("tagId"))
                .tagName(rs.getString("tag_name"))
                .build();
    }
}
