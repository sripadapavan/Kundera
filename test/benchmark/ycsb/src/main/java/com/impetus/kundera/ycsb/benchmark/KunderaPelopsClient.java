/**
 * Copyright 2012 Impetus Infotech.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.impetus.kundera.ycsb.benchmark;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.impetus.kundera.ycsb.entities.PelopsUser;
import com.yahoo.ycsb.ByteIterator;
import com.yahoo.ycsb.DB;
import com.yahoo.ycsb.DBException;
import common.Logger;

/**
 * @author Kuldeep Mishra
 * 
 */
public class KunderaPelopsClient extends DB
{

    private static Logger logger = Logger.getLogger(KunderaPelopsClient.class);

//    static Random random = new Random();

    private static final int Ok = 0;

    private static final int Error = -1;

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("kundera_pelops_pu");

    private EntityManager em;

    private int j;

    /**
     * Initialize any state for this DB. Called once per DB instance; there is
     * one DB instance per client thread.
     */
    public void init() throws DBException
    {
        em = emf.createEntityManager();
        j = 1;
    }

    /**
     * Cleanup any state for this DB. Called once per DB instance; there is one
     * DB instance per client thread.
     */
    public void cleanup() throws DBException
    {
        em.clear();
//        em.close();
    }

    /**
     * Read a record from the database. Each field/value pair from the result
     * will be stored in a HashMap.
     * 
     * @param table
     *            The name of the table
     * @param key
     *            The record key of the record to read.
     * @param fields
     *            The list of fields to read, or null for all of them
     * @param result
     *            A HashMap of field/value pairs for the result
     * @return Zero on success, a non-zero error code on error
     */
    public int read(String table, String key, Set<String> fields, HashMap<String, ByteIterator> result)
    {
        try
        {
            Object o = em.find(PelopsUser.class, key);
            assert o != null;
            j++;
            em.clear();
//            if (j % 5000 == 0)
//            {
//                em.clear();
//             }
            return Ok;
        }
        catch (Exception e)
        {
            logger.error(e);
            return Error;
        }
    }

    /**
     * Perform a range scan for a set of records in the database. Each
     * field/value pair from the result will be stored in a HashMap.
     * 
     * @param table
     *            The name of the table
     * @param startkey
     *            The record key of the first record to read.
     * @param recordcount
     *            The number of records to read
     * @param fields
     *            The list of fields to read, or null for all of them
     * @param result
     *            A Vector of HashMaps, where each HashMap is a set field/value
     *            pairs for one record
     * @return Zero on success, a non-zero error code on error
     */
    public int scan(String table, String startkey, int recordcount, Set<String> fields,
            Vector<HashMap<String, ByteIterator>> result)
    {
        return Ok;
    }

    /**
     * Update a record in the database. Any field/value pairs in the specified
     * values HashMap will be written into the record with the specified record
     * key, overwriting any existing values with the same field name.
     * 
     * @param table
     *            The name of the table
     * @param key
     *            The record key of the record to write.
     * @param values
     *            A HashMap of field/value pairs to update in the record
     * @return Zero on success, a non-zero error code on error
     */
    public int update(String table, String key, HashMap<String, ByteIterator> values)
    {
        return insert(table, key, values);
    }

    /**
     * Insert a record in the database. Any field/value pairs in the specified
     * values HashMap will be written into the record with the specified record
     * key.
     * 
     * @param table
     *            The name of the table
     * @param key
     *            The record key of the record to insert.
     * @param values
     *            A HashMap of field/value pairs to insert in the record
     * @return Zero on success, a non-zero error code on error
     */
    public int insert(String table, String key, HashMap<String, ByteIterator> values)
    {
        try
        {
            PelopsUser u = new PelopsUser(key, getString(key, "24"), getString(key, "gzb"), getString(key, "mishra"));
            em.persist(u);
//            j++;
//            if (j % 5000 == 0)
//            {
                em.clear();
//             }
            
                return Ok;
        }
        catch (Exception e)
        {
            logger.error(e);
            return Error;
        }
    }

    /**
     * Delete a record from the database.
     * 
     * @param table
     *            The name of the table
     * @param key
     *            The record key of the record to delete.
     * @return Zero on success, a non-zero error code on error
     */
    public int delete(String table, String key)
    {
        try
        {
            em.remove(em.find(PelopsUser.class, key));
            return Ok;
        }
        catch (Exception e)
        {
            logger.error(e);
            return Error;
        }
    }

    private String getString(String key, String value)
    {
        StringBuilder builder = new StringBuilder(key);
        builder.append(value);
        return builder.toString();
    }
}
