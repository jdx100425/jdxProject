package com.maoshen.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


public class BeanValidatorUtil {
	/**
	 * VO Validation 校验
	 * @param T
	 * @return
	 */
	public  static <M extends Object> Map<Path,Object> havaValidator(M T){
		ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
		Map<Path,Object> validatorMap = new HashMap<Path,Object>();
		Validator validator = vf.getValidator();
		Set<ConstraintViolation<M>> set = validator.validate(T);
		for (ConstraintViolation<M> constraintViolation : set) {
			validatorMap.put(constraintViolation.getPropertyPath(), constraintViolation.getMessage());
		}
		return validatorMap;
	}
	/*
	*//**
	 * VO 单个字段 Validation 校验
	 * @param T
	 * @return
	 *//*
	public  static <M extends Object> Map<Path,Object> havaValidator(M T,String feid){
		ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
		Map<Path,Object> validatorMap = new HashMap<Path,Object>();
		Validator validator = vf.getValidator();
		Set<ConstraintViolation<M>> set = validator.validate(T);		
		for (ConstraintViolation<M> constraintViolation : set) {
			if(constraintViolation.getPropertyPath().equals(feid)){
				validatorMap.put(constraintViolation.getPropertyPath(), constraintViolation.getMessage());	
			}
			
		}
		return validatorMap;
	}
	*//**
	 * VO 部分字段 Validation 校验
	 * @param T
	 * @return
	 *//*
	public  static <M extends Object> Map<Path,Object> havaValidator(M T,Map<String,String> feidMap){
		ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
		Map<Path,Object> validatorMap = new HashMap<Path,Object>();
		Validator validator = vf.getValidator();
		Set<ConstraintViolation<M>> set = validator.validate(T);		
		for (ConstraintViolation<M> constraintViolation : set) {
			if(feidMap.containsKey(constraintViolation.getPropertyPath())){
				validatorMap.put(constraintViolation.getPropertyPath(), constraintViolation.getMessage());	
			}
			
		}
		return validatorMap;
	}*/
}