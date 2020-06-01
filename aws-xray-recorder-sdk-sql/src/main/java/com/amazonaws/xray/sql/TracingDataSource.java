/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amazonaws.xray.sql;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class TracingDataSource implements DataSource {

    protected DataSource delegate;

    public TracingDataSource(DataSource dataSource) {
        this.delegate = dataSource;
    }

    /**
     * Call {@code dataSource = TracingDataSource.decorate(dataSource)} to decorate your {@link DataSource} before any calls
     * to #getConnection in order to have all your SQL queries recorded with an X-Ray Subsegment.
     *
     * @param dataSource the datasource to decorate
     * @return a DataSource that traces all SQL queries in X-Ray
     */
    public static DataSource decorate(DataSource dataSource) {
        return new TracingDataSource(dataSource);
    }

    /**
     *  Traced methods
     */

    @Override
    public Connection getConnection() throws SQLException {
        return new TracingConnection(delegate.getConnection());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return new TracingConnection(delegate.getConnection(username, password));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return (T) this;
        }
        return delegate.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return (iface.isInstance(this) || delegate.isWrapperFor(iface));
    }

    /**
     * Plain methods
     */

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return delegate.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        delegate.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        delegate.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return delegate.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return delegate.getParentLogger();
    }
}