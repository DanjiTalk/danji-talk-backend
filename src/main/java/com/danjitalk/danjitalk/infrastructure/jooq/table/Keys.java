/*
 * This file is generated by jOOQ.
 */
package com.danjitalk.danjitalk.infrastructure.jooq.table;


import com.danjitalk.danjitalk.infrastructure.jooq.table.tables.Bookmark;
import com.danjitalk.danjitalk.infrastructure.jooq.table.tables.Feed;
import com.danjitalk.danjitalk.infrastructure.jooq.table.tables.Reaction;
import com.danjitalk.danjitalk.infrastructure.jooq.table.tables.records.BookmarkRecord;
import com.danjitalk.danjitalk.infrastructure.jooq.table.tables.records.FeedRecord;
import com.danjitalk.danjitalk.infrastructure.jooq.table.tables.records.ReactionRecord;

import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * danjitalk.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<BookmarkRecord> KEY_BOOKMARK_PRIMARY = Internal.createUniqueKey(Bookmark.BOOKMARK, DSL.name("KEY_bookmark_PRIMARY"), new TableField[] { Bookmark.BOOKMARK.ID }, true);
    public static final UniqueKey<BookmarkRecord> KEY_BOOKMARK_UK5U0YIDGDJ1AYX5HXL0POD0JOV = Internal.createUniqueKey(Bookmark.BOOKMARK, DSL.name("KEY_bookmark_UK5u0yidgdj1ayx5hxl0pod0jov"), new TableField[] { Bookmark.BOOKMARK.MEMBER_ID, Bookmark.BOOKMARK.FEED_ID }, true);
    public static final UniqueKey<FeedRecord> KEY_FEED_PRIMARY = Internal.createUniqueKey(Feed.FEED, DSL.name("KEY_feed_PRIMARY"), new TableField[] { Feed.FEED.ID }, true);
    public static final UniqueKey<ReactionRecord> KEY_REACTION_PRIMARY = Internal.createUniqueKey(Reaction.REACTION, DSL.name("KEY_reaction_PRIMARY"), new TableField[] { Reaction.REACTION.ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<ReactionRecord, FeedRecord> FKHGEKJDM7K2IAB2NLTI5P99EU9 = Internal.createForeignKey(Reaction.REACTION, DSL.name("FKhgekjdm7k2iab2nlti5p99eu9"), new TableField[] { Reaction.REACTION.FEED_ID }, Keys.KEY_FEED_PRIMARY, new TableField[] { Feed.FEED.ID }, true);
}
