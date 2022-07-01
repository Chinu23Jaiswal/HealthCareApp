package com.healthcareApp.healthcareApp.config;


import com.healthcareApp.healthcareApp.exceptions.UnableToProcessException;
import com.healthcareApp.healthcareApp.entity.Doctor;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomIdGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        boolean isDoctor = object instanceof Doctor;
        String prefix = isDoctor ? "DOC" : "PAT";
        String table = isDoctor ? "doctor" : "patient";

        Connection connection = session.connection();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select count(id) as Id from " + table);
            if (rs.next()) {
                int id = rs.getInt(1) + 101;
                return prefix + id;
            }
        } catch (SQLException e) {
            throw new UnableToProcessException(e.getMessage());
        }
        return null;
    }
}
