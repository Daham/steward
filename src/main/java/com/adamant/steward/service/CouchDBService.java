package com.adamant.steward.service;

import com.adamant.steward.entity.couch_db.CouchDBFindAllResponse;
import com.adamant.steward.entity.user_management.CouchDocument;

public interface CouchDBService {

    <T extends CouchDocument> boolean save(T t);

    <T> boolean update(T t);

    <T> T find(Class<T> classType, String id);

    <T> CouchDBFindAllResponse<T> findAll(Class<T> classType, int rowsPerPage, String param);

    boolean remove(String id, String rev);

    boolean replicate(String source, String target);
}
