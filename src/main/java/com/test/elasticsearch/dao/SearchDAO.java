package com.test.elasticsearch.dao;

import com.test.elasticsearch.dto.SearchParameters;
import com.test.elasticsearch.mapper.QueryRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SearchDAO {




    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SearchDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<SearchParameters> getSearchData(String locationId ,List<String> itemIds){
        String sqlQuery;
        if(StringUtils.isEmpty(locationId) && itemIds.isEmpty()) {
            sqlQuery = "SELECT l.id locationId,l.name locationName,l.city,i.id itemId,i.name," +
                    "i.sell_price,m.id imageId,m.mime_type,t.id tagId,t.tag_name " +
                    "from mh_location l " +
                    "inner join mh_items i on l.id = i.location_id " +
                    "left join mh_media m on i.id = m.entity_id and m.entity_type = 'ITEM' " +
                    "left join mh_item_tags it on i.id = it.item_id " +
                    "left join mh_tags t on it.tag_id = t.id";
            return namedParameterJdbcTemplate.query(sqlQuery,new QueryRowMapper());
        }else if(StringUtils.isEmpty(locationId)){
            SqlParameterSource parameters = new MapSqlParameterSource("itemIds", itemIds);
            sqlQuery = "SELECT l.id locationId,l.name locationName,l.city,i.id itemId,i.name," +
                    "i.sell_price,m.id imageId,m.mime_type,t.id tagId,t.tag_name " +
                    "from mh_location l " +
                    "inner join mh_items i on l.id = i.location_id " +
                    "left join mh_media m on i.id = m.entity_id and m.entity_type = 'ITEM' " +
                    "left join mh_item_tags it on i.id = it.item_id " +
                    "left join mh_tags t on it.tag_id = t.id" +
                    " where  i.id in (:itemIds)";
            return namedParameterJdbcTemplate.query(sqlQuery,parameters,new QueryRowMapper());
        }else {
            SqlParameterSource parameters = new MapSqlParameterSource("locationId", locationId);
            sqlQuery = "SELECT l.id locationId,l.name locationName,l.city,i.id itemId,i.name," +
                    "i.sell_price, m.id imageId,m.mime_type,t.id tagId,t.tag_name " +
                    "from mh_location l " +
                    "inner join mh_items i on l.id = i.location_id " +
                    "left join mh_media m on i.id = m.entity_id and m.entity_type = 'ITEM' " +
                    "left join mh_item_tags it on i.id = it.item_id " +
                    "left join mh_tags t on it.tag_id = t.id" +
                    " where  l.id =:locationId";
            return namedParameterJdbcTemplate.query(sqlQuery,parameters,new QueryRowMapper());
        }
    }
}
