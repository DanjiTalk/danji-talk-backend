/*
 * This file is generated by jOOQ.
 */
package com.danjitalk.danjitalk.infrastructure.jooq.table.tables.records;


import com.danjitalk.danjitalk.domain.community.reaction.enums.ReactionType;
import com.danjitalk.danjitalk.infrastructure.jooq.table.tables.Reaction;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ReactionRecord extends UpdatableRecordImpl<ReactionRecord> implements Record4<Long, ReactionType, Long, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>danjitalk.reaction.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>danjitalk.reaction.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>danjitalk.reaction.reaction_type</code>.
     */
    public void setReactionType(ReactionType value) {
        set(1, value);
    }

    /**
     * Getter for <code>danjitalk.reaction.reaction_type</code>.
     */
    public ReactionType getReactionType() {
        return (ReactionType) get(1);
    }

    /**
     * Setter for <code>danjitalk.reaction.feed_id</code>.
     */
    public void setFeedId(Long value) {
        set(2, value);
    }

    /**
     * Getter for <code>danjitalk.reaction.feed_id</code>.
     */
    public Long getFeedId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>danjitalk.reaction.member_id</code>.
     */
    public void setMemberId(Long value) {
        set(3, value);
    }

    /**
     * Getter for <code>danjitalk.reaction.member_id</code>.
     */
    public Long getMemberId() {
        return (Long) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<Long, ReactionType, Long, Long> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<Long, ReactionType, Long, Long> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Reaction.REACTION.ID;
    }

    @Override
    public Field<ReactionType> field2() {
        return Reaction.REACTION.REACTION_TYPE;
    }

    @Override
    public Field<Long> field3() {
        return Reaction.REACTION.FEED_ID;
    }

    @Override
    public Field<Long> field4() {
        return Reaction.REACTION.MEMBER_ID;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public ReactionType component2() {
        return getReactionType();
    }

    @Override
    public Long component3() {
        return getFeedId();
    }

    @Override
    public Long component4() {
        return getMemberId();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public ReactionType value2() {
        return getReactionType();
    }

    @Override
    public Long value3() {
        return getFeedId();
    }

    @Override
    public Long value4() {
        return getMemberId();
    }

    @Override
    public ReactionRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public ReactionRecord value2(ReactionType value) {
        setReactionType(value);
        return this;
    }

    @Override
    public ReactionRecord value3(Long value) {
        setFeedId(value);
        return this;
    }

    @Override
    public ReactionRecord value4(Long value) {
        setMemberId(value);
        return this;
    }

    @Override
    public ReactionRecord values(Long value1, ReactionType value2, Long value3, Long value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ReactionRecord
     */
    public ReactionRecord() {
        super(Reaction.REACTION);
    }

    /**
     * Create a detached, initialised ReactionRecord
     */
    public ReactionRecord(Long id, ReactionType reactionType, Long feedId, Long memberId) {
        super(Reaction.REACTION);

        setId(id);
        setReactionType(reactionType);
        setFeedId(feedId);
        setMemberId(memberId);
        resetChangedOnNotNull();
    }
}
