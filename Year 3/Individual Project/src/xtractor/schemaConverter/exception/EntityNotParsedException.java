/*
 * Created on 20-May-2004
 *
 */
package xtractor.schem= onverter.exception;

import xtractor.schemaConverter.xer.xerConstructs.XEREntity;

/**
 * @author ss401
 *
 */
public class EntityNotParsedException extends Exception {

	XEREntity entity;

	public EntityNotParsedException(XEREntity entity) {
		this.entity = entity;
	}

	public XEREntity getMissingEntity() {

		return entity;
	}

}
