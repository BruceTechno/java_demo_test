package com.example.java_demo_test.repository;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.util.CollectionUtils;

import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;





public class BaseDao {
	@PersistenceContext
	private EntityManager entityManager;
	
	
	@SuppressWarnings("unchecked")
	protected <EntityType> List<EntityType> doQuery(String sql , Map<String,Object> params,
			Class<EntityType> clazz){
		//Query query = entityManager.createQuery(sql,clazz);
		//-------------------step 2 優化 limitSize 用不到 帶-1讓if不成立
//	if (!CollectionUtils.isEmpty(params)) {
//		for( Entry<String, Object> item : params.entrySet()) {
//			query.setParameter( item.getKey(), item.getValue());
//		}
//		for(Parameter p:query.getParameters()) {
//		query.setParameter(p,params.get(p.getName()));
//	}//用上面的寫法就好了 
//	}													--------- step 2 優化
		
                                            step 1
		
	
		return doQuery(sql , params, clazz ,-1);
	}
	/*
	 * 限制回傳筆數
	 */
	@SuppressWarnings("unchecked")
	protected <EntityType> List<EntityType> doQuery(String sql , Map<String,Object> params,
			Class<EntityType> clazz , int limitSize){
//		Query query = entityManager.createQuery(sql,clazz);
//		doQuery(sql ,params,clazz , limitSize, -1);
		
//		if (!CollectionUtils.isEmpty(params)) {
//			for( Entry<String, Object> item : params.entrySet()) {
//				query.setParameter( item.getKey(), item.getValue());
//			}
//
//		}
//		if (limitSize > 0) {
//			query.setMaxResults(limitSize);
//		
//		}
		return doQuery(sql ,params,clazz , limitSize, -1);
	}
	/*
	 * limitSize 限制回傳筆數
	 * startPosition 每一頁的起始位置
	 */
	@SuppressWarnings("unchecked")
	protected <EntityType> List<EntityType> doQuery(String sql , Map<String,Object> params,
			Class<EntityType> clazz , int limitSize,int startPosition){
		Query query = entityManager.createQuery(sql,clazz);
		if (!CollectionUtils.isEmpty(params)) {
			for( Entry<String, Object> item : params.entrySet()) {
				query.setParameter( item.getKey(), item.getValue());
			}
		}
		if(limitSize>0) {
			query.setMaxResults(limitSize);
		}
		if (startPosition >= 0) {
			query.setFirstResult(startPosition);
		}
		
		for( Entry<String, Object> item : params.entrySet()) {
			query.setParameter( item.getKey(), item.getValue());
		}
		
		return query.getResultList();
	}
	protected int doUpdate(String sql , Map<String,Object> params) {
		Query query = entityManager.createQuery(sql);

		if (!CollectionUtils.isEmpty(params)) {
			for( Entry<String, Object> item : params.entrySet()) {
				query.setParameter( item.getKey(), item.getValue());
			}
		}
		return query.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	protected <EntityType> List<EntityType> doNativeQuery(String sql , Map<String,Object> params,
			Class<EntityType> clazz , int limitSize,int startPosition){
		Query query = entityManager.createQuery(sql,clazz);
		if (!CollectionUtils.isEmpty(params)) {
			for( Entry<String, Object> item : params.entrySet()) {
				query.setParameter( item.getKey(), item.getValue());
			}
		}
		if(limitSize>0) {
			query.setMaxResults(limitSize);
		}
		if (startPosition >= 0) {
			query.setFirstResult(startPosition);
		}
		
		for( Entry<String, Object> item : params.entrySet()) {
			query.setParameter( item.getKey(), item.getValue());
		}
	
}
