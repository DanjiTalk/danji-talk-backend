/*
 * This file is generated by jOOQ.
 */
package com.danjitalk.danjitalk.infrastructure.jooq.table.tables.records;


import com.danjitalk.danjitalk.infrastructure.jooq.table.tables.Feed;

import java.time.LocalDateTime;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record14;
import org.jooq.Row14;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FeedRecord extends UpdatableRecordImpl<FeedRecord> implements Record14<Long, LocalDateTime, LocalDateTime, Integer, Integer, String, String, String, Integer, String, String, Integer, Long, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>danjitalk.feed.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>danjitalk.feed.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>danjitalk.feed.created_at</code>.
     */
    public void setCreatedAt(LocalDateTime value) {
        set(1, value);
    }

    /**
     * Getter for <code>danjitalk.feed.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(1);
    }

    /**
     * Setter for <code>danjitalk.feed.updated_at</code>.
     */
    public void setUpdatedAt(LocalDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>danjitalk.feed.updated_at</code>.
     */
    public LocalDateTime getUpdatedAt() {
        return (LocalDateTime) get(2);
    }

    /**
     * Setter for <code>danjitalk.feed.bookmark_count</code>.
     */
    public void setBookmarkCount(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>danjitalk.feed.bookmark_count</code>.
     */
    public Integer getBookmarkCount() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>danjitalk.feed.comment_count</code>.
     */
    public void setCommentCount(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>danjitalk.feed.comment_count</code>.
     */
    public Integer getCommentCount() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>danjitalk.feed.contents</code>.
     */
    public void setContents(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>danjitalk.feed.contents</code>.
     */
    public String getContents() {
        return (String) get(5);
    }

    /**
     * Setter for <code>danjitalk.feed.feed_type</code>.
     */
    public void setFeedType(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>danjitalk.feed.feed_type</code>.
     */
    public String getFeedType() {
        return (String) get(6);
    }

    /**
     * Setter for <code>danjitalk.feed.file_url</code>.
     */
    public void setFileUrl(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>danjitalk.feed.file_url</code>.
     */
    public String getFileUrl() {
        return (String) get(7);
    }

    /**
     * Setter for <code>danjitalk.feed.reaction_count</code>.
     */
    public void setReactionCount(Integer value) {
        set(8, value);
    }

    /**
     * Getter for <code>danjitalk.feed.reaction_count</code>.
     */
    public Integer getReactionCount() {
        return (Integer) get(8);
    }

    /**
     * Setter for <code>danjitalk.feed.thumbnail_file_url</code>.
     */
    public void setThumbnailFileUrl(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>danjitalk.feed.thumbnail_file_url</code>.
     */
    public String getThumbnailFileUrl() {
        return (String) get(9);
    }

    /**
     * Setter for <code>danjitalk.feed.title</code>.
     */
    public void setTitle(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>danjitalk.feed.title</code>.
     */
    public String getTitle() {
        return (String) get(10);
    }

    /**
     * Setter for <code>danjitalk.feed.view_count</code>.
     */
    public void setViewCount(Integer value) {
        set(11, value);
    }

    /**
     * Getter for <code>danjitalk.feed.view_count</code>.
     */
    public Integer getViewCount() {
        return (Integer) get(11);
    }

    /**
     * Setter for <code>danjitalk.feed.apartment_id</code>.
     */
    public void setApartmentId(Long value) {
        set(12, value);
    }

    /**
     * Getter for <code>danjitalk.feed.apartment_id</code>.
     */
    public Long getApartmentId() {
        return (Long) get(12);
    }

    /**
     * Setter for <code>danjitalk.feed.member_id</code>.
     */
    public void setMemberId(Long value) {
        set(13, value);
    }

    /**
     * Getter for <code>danjitalk.feed.member_id</code>.
     */
    public Long getMemberId() {
        return (Long) get(13);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record14 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row14<Long, LocalDateTime, LocalDateTime, Integer, Integer, String, String, String, Integer, String, String, Integer, Long, Long> fieldsRow() {
        return (Row14) super.fieldsRow();
    }

    @Override
    public Row14<Long, LocalDateTime, LocalDateTime, Integer, Integer, String, String, String, Integer, String, String, Integer, Long, Long> valuesRow() {
        return (Row14) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Feed.FEED.ID;
    }

    @Override
    public Field<LocalDateTime> field2() {
        return Feed.FEED.CREATED_AT;
    }

    @Override
    public Field<LocalDateTime> field3() {
        return Feed.FEED.UPDATED_AT;
    }

    @Override
    public Field<Integer> field4() {
        return Feed.FEED.BOOKMARK_COUNT;
    }

    @Override
    public Field<Integer> field5() {
        return Feed.FEED.COMMENT_COUNT;
    }

    @Override
    public Field<String> field6() {
        return Feed.FEED.CONTENTS;
    }

    @Override
    public Field<String> field7() {
        return Feed.FEED.FEED_TYPE;
    }

    @Override
    public Field<String> field8() {
        return Feed.FEED.FILE_URL;
    }

    @Override
    public Field<Integer> field9() {
        return Feed.FEED.REACTION_COUNT;
    }

    @Override
    public Field<String> field10() {
        return Feed.FEED.THUMBNAIL_FILE_URL;
    }

    @Override
    public Field<String> field11() {
        return Feed.FEED.TITLE;
    }

    @Override
    public Field<Integer> field12() {
        return Feed.FEED.VIEW_COUNT;
    }

    @Override
    public Field<Long> field13() {
        return Feed.FEED.APARTMENT_ID;
    }

    @Override
    public Field<Long> field14() {
        return Feed.FEED.MEMBER_ID;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public LocalDateTime component2() {
        return getCreatedAt();
    }

    @Override
    public LocalDateTime component3() {
        return getUpdatedAt();
    }

    @Override
    public Integer component4() {
        return getBookmarkCount();
    }

    @Override
    public Integer component5() {
        return getCommentCount();
    }

    @Override
    public String component6() {
        return getContents();
    }

    @Override
    public String component7() {
        return getFeedType();
    }

    @Override
    public String component8() {
        return getFileUrl();
    }

    @Override
    public Integer component9() {
        return getReactionCount();
    }

    @Override
    public String component10() {
        return getThumbnailFileUrl();
    }

    @Override
    public String component11() {
        return getTitle();
    }

    @Override
    public Integer component12() {
        return getViewCount();
    }

    @Override
    public Long component13() {
        return getApartmentId();
    }

    @Override
    public Long component14() {
        return getMemberId();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public LocalDateTime value2() {
        return getCreatedAt();
    }

    @Override
    public LocalDateTime value3() {
        return getUpdatedAt();
    }

    @Override
    public Integer value4() {
        return getBookmarkCount();
    }

    @Override
    public Integer value5() {
        return getCommentCount();
    }

    @Override
    public String value6() {
        return getContents();
    }

    @Override
    public String value7() {
        return getFeedType();
    }

    @Override
    public String value8() {
        return getFileUrl();
    }

    @Override
    public Integer value9() {
        return getReactionCount();
    }

    @Override
    public String value10() {
        return getThumbnailFileUrl();
    }

    @Override
    public String value11() {
        return getTitle();
    }

    @Override
    public Integer value12() {
        return getViewCount();
    }

    @Override
    public Long value13() {
        return getApartmentId();
    }

    @Override
    public Long value14() {
        return getMemberId();
    }

    @Override
    public FeedRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public FeedRecord value2(LocalDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    public FeedRecord value3(LocalDateTime value) {
        setUpdatedAt(value);
        return this;
    }

    @Override
    public FeedRecord value4(Integer value) {
        setBookmarkCount(value);
        return this;
    }

    @Override
    public FeedRecord value5(Integer value) {
        setCommentCount(value);
        return this;
    }

    @Override
    public FeedRecord value6(String value) {
        setContents(value);
        return this;
    }

    @Override
    public FeedRecord value7(String value) {
        setFeedType(value);
        return this;
    }

    @Override
    public FeedRecord value8(String value) {
        setFileUrl(value);
        return this;
    }

    @Override
    public FeedRecord value9(Integer value) {
        setReactionCount(value);
        return this;
    }

    @Override
    public FeedRecord value10(String value) {
        setThumbnailFileUrl(value);
        return this;
    }

    @Override
    public FeedRecord value11(String value) {
        setTitle(value);
        return this;
    }

    @Override
    public FeedRecord value12(Integer value) {
        setViewCount(value);
        return this;
    }

    @Override
    public FeedRecord value13(Long value) {
        setApartmentId(value);
        return this;
    }

    @Override
    public FeedRecord value14(Long value) {
        setMemberId(value);
        return this;
    }

    @Override
    public FeedRecord values(Long value1, LocalDateTime value2, LocalDateTime value3, Integer value4, Integer value5, String value6, String value7, String value8, Integer value9, String value10, String value11, Integer value12, Long value13, Long value14) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        value14(value14);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached FeedRecord
     */
    public FeedRecord() {
        super(Feed.FEED);
    }

    /**
     * Create a detached, initialised FeedRecord
     */
    public FeedRecord(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, Integer bookmarkCount, Integer commentCount, String contents, String feedType, String fileUrl, Integer reactionCount, String thumbnailFileUrl, String title, Integer viewCount, Long apartmentId, Long memberId) {
        super(Feed.FEED);

        setId(id);
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setBookmarkCount(bookmarkCount);
        setCommentCount(commentCount);
        setContents(contents);
        setFeedType(feedType);
        setFileUrl(fileUrl);
        setReactionCount(reactionCount);
        setThumbnailFileUrl(thumbnailFileUrl);
        setTitle(title);
        setViewCount(viewCount);
        setApartmentId(apartmentId);
        setMemberId(memberId);
        resetChangedOnNotNull();
    }
}
