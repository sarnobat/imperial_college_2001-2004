/*
 * Created on 27-May-2004
 *
 */
package xtractor.schemaConverter.xer.xerConstructs;

import xtractor.schemaConverter.xer.XERBuilder;

/**
 * @author ss401
 *
 */
public class XERMixedEntity extends XEREntity {

	boolean mixedContentEnabled;

	/**
	 * @param name
	 * @param xerBuilder
	 */
	public XERMixedEntity(String name, XERBuilder xerBuilder) {
		super(name, xerBuilder);
		setPromoted();
	}

	/**
	 * Replaces this mixed entity with a plain entity
	 * 
	 * This is not a reversible operation
	 */
	// This is too complicated
	private void demote() {
		XEREntity demotedEntity = (XEREntity) this;
		xerBuilder.getModel().replaceEntity(this, demotedEntity);
		
		
		if (demotedEntity instanceof XERMixedEntity) {
			logger.fatal("Demotion of mixed entity not working.");
		}
	}

	/**
	 * Sets mixedContent enabled
	 */
	public void setPromoted() {
		this.mixedContentEnabled = true;
		xerBuilder.getModel().removeDemotedMixedEntity(this);
		
	}

	/**
	 * Sets mixedContent disabled
	 */
	public void setDemoted() {
		this.mixedContentEnabled = false;
		xerBuilder.getModel().addDemotedMixedEntity(this);

	}

	/**
	 * @return - True if mixed content is enabled.
	 */
	public boolean isMixedContentEnabled() {
		return mixedContentEnabled;
	}

}
