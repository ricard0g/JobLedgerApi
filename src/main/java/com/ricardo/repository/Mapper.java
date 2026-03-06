package com.ricardo.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Mapper<T> {
    protected abstract T mapRow(ResultSet rs) throws SQLException;
}
