//package com.danjitalk.danjitalk.application.community.bookmark;
//
//import com.danjitalk.danjitalk.application.community.feed.FeedService;
//import com.danjitalk.danjitalk.common.security.CustomMemberDetails;
//import com.danjitalk.danjitalk.domain.apartment.entity.Apartment;
//import com.danjitalk.danjitalk.domain.community.bookmark.dto.request.BookmarkRequestDto;
//import com.danjitalk.danjitalk.domain.community.bookmark.entity.Bookmark;
//import com.danjitalk.danjitalk.domain.community.feed.entity.Feed;
//import com.danjitalk.danjitalk.domain.community.feed.enums.FeedType;
//import com.danjitalk.danjitalk.domain.user.member.entity.Member;
//import com.danjitalk.danjitalk.domain.user.member.entity.SystemUser;
//import com.danjitalk.danjitalk.domain.user.member.enums.LoginMethod;
//import com.danjitalk.danjitalk.domain.user.member.enums.Role;
//import com.danjitalk.danjitalk.infrastructure.repository.apartment.ApartmentRepository;
//import com.danjitalk.danjitalk.infrastructure.repository.community.bookmark.BookmarkRepository;
//import com.danjitalk.danjitalk.infrastructure.repository.community.feed.FeedRepository;
//import com.danjitalk.danjitalk.infrastructure.repository.user.member.MemberRepository;
//import com.danjitalk.danjitalk.infrastructure.repository.user.member.SystemUserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class BookmarkServiceTest {
//
//    @Autowired private BookmarkService bookmarkService;
//    @Autowired private BookmarkRepository bookmarkRepository;
//    @Autowired private FeedService feedService;
//    @Autowired private FeedRepository feedRepository;
//    @Autowired private ApartmentRepository apartmentRepository;
//    @Autowired private MemberRepository memberRepository;
//    @Autowired private SystemUserRepository systemUserRepository;
//    @Autowired private BCryptPasswordEncoder passwordEncoder;;
//
//    @BeforeEach
//    void setUp() {
//        Member member = new Member("aaa@aaa.com", "유동엽", "010-0000-1111", null, null, null, null, null, "Yu");
//        memberRepository.save(member);
//
//        Apartment apartment = new Apartment("서울 자이아파트", "동작구", null, null, null, null, null, null, null);
//        apartmentRepository.save(apartment);
//
//        Feed feed = new Feed("타이틀", "콘텐츠", FeedType.FEED, null, null, member, apartment);
//        feedRepository.save(feed);
//
//        SystemUser systemUser = new SystemUser(Role.USER, LoginMethod.NORMAL, null, passwordEncoder.encode("123124512312312adasdas"), "aaa", member);
//        systemUserRepository.save(systemUser);
//
//        // SecurityContextHolder 에 저장
//        CustomMemberDetails principal = new CustomMemberDetails(systemUser, null);
//        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//    }
//
//    @Test
//    void isBookmarked() {
//        Boolean bookmarked = bookmarkService.isBookmarked(1L);
//        assertFalse(bookmarked);
//    }
//
//    @Test
//    void addBookmark() {
//        BookmarkRequestDto bookmarkRequestDto = new BookmarkRequestDto(1L);
//        bookmarkService.addBookmark(bookmarkRequestDto);
//
//        List<Bookmark> all = bookmarkRepository.findAll();
//
//        assertEquals(1, all.size());
//        assertEquals(Bookmark.class, all.get(0).getClass());
//
//    }
//
//    @Test
//    void deleteBookmark() {
//        BookmarkRequestDto bookmarkRequestDto = new BookmarkRequestDto(1L);
//        bookmarkService.addBookmark(bookmarkRequestDto);
//
//        List<Bookmark> all = bookmarkRepository.findAll();
//
//        assertEquals(1, all.size());
//
//        BookmarkRequestDto bookmarkRequestDto2 = new BookmarkRequestDto(1L);
//        bookmarkService.deleteBookmark(bookmarkRequestDto2);
//
//        List<Bookmark> all2 = bookmarkRepository.findAll();
//
//        assertEquals(0, all2.size());
//
//    }
//}