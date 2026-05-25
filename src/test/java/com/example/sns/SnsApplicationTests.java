package com.example.sns;

import com.example.sns.dto.CommentCreateRequest;
import com.example.sns.dto.CommentResponse;
import com.example.sns.dto.CommentUpdateRequest;
import com.example.sns.entity.Post;
import com.example.sns.entity.User;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;
import com.example.sns.service.CommentCommandService;
import com.example.sns.service.CommentQueryService;
import com.example.sns.service.LikeCommandService;
import com.example.sns.service.LikeQueryService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SnsApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private CommentCommandService commentCommandService;

	@Autowired
	private CommentQueryService commentQueryService;

	@Autowired
	private LikeCommandService likeCommandService;

	@Autowired
	private LikeQueryService likeQueryService;

	@Autowired
	private EntityManager entityManager;

	@Test
	@DisplayName("댓글을 생성할 수 있다")
	void createComment_success() {
		// given
		User postAuthor = userRepository.save(User.create("post-author"));
		User commenter = userRepository.save(User.create("commenter"));
		Post post = postRepository.save(Post.create(postAuthor, "게시글 제목", "게시글 내용"));

		CommentCreateRequest request = new CommentCreateRequest(
				commenter.getId(),
				"첫 번째 댓글입니다."
		);

		// when
		commentCommandService.addComment(post.getId(), request);

		entityManager.flush();
		entityManager.clear();

		List<CommentResponse> comments = commentQueryService.getComments(post.getId());

		// then
		assertEquals(1, comments.size());

		CommentResponse comment = comments.get(0);
		assertNotNull(comment.id());
		assertEquals(post.getId(), comment.postId());
		assertEquals(commenter.getId(), comment.authorId());
		assertEquals("commenter", comment.authorName());
		assertEquals("첫 번째 댓글입니다.", comment.content());
	}

	@Test
	@DisplayName("댓글을 조회할 수 있다")
	void findComments_success() {
		// given
		User postAuthor = userRepository.save(User.create("post-author"));
		User commenter1 = userRepository.save(User.create("commenter1"));
		User commenter2 = userRepository.save(User.create("commenter2"));
		Post post = postRepository.save(Post.create(postAuthor, "게시글 제목", "게시글 내용"));

		commentCommandService.addComment(
				post.getId(),
				new CommentCreateRequest(commenter1.getId(), "첫 번째 댓글")
		);

		commentCommandService.addComment(
				post.getId(),
				new CommentCreateRequest(commenter2.getId(), "두 번째 댓글")
		);

		entityManager.flush();
		entityManager.clear();

		// when
		List<CommentResponse> comments = commentQueryService.getComments(post.getId());

		// then
		assertEquals(2, comments.size());

		assertEquals("첫 번째 댓글", comments.get(0).content());
		assertEquals("두 번째 댓글", comments.get(1).content());
	}

	@Test
	@DisplayName("댓글을 편집한 뒤 조회하면 수정된 내용이 보인다")
	void editCommentAndFind_success() {
		// given
		User postAuthor = userRepository.save(User.create("post-author"));
		User commenter = userRepository.save(User.create("commenter"));
		Post post = postRepository.save(Post.create(postAuthor, "게시글 제목", "게시글 내용"));

		commentCommandService.addComment(
				post.getId(),
				new CommentCreateRequest(commenter.getId(), "수정 전 댓글")
		);

		entityManager.flush();
		entityManager.clear();

		Long commentId = commentQueryService.getComments(post.getId())
				.get(0)
				.id();

		CommentUpdateRequest updateRequest = new CommentUpdateRequest(
				commenter.getId(),
				"수정 후 댓글"
		);

		// when
		commentCommandService.editComment(post.getId(), commentId, updateRequest);

		entityManager.flush();
		entityManager.clear();

		List<CommentResponse> comments = commentQueryService.getComments(post.getId());

		// then
		assertEquals(1, comments.size());
		assertEquals(commentId, comments.get(0).id());
		assertEquals("수정 후 댓글", comments.get(0).content());
	}

	@Test
	@DisplayName("댓글을 삭제한 뒤 조회하면 목록에서 사라진다")
	void deleteCommentAndFind_success() {
		// given
		User postAuthor = userRepository.save(User.create("post-author"));
		User commenter = userRepository.save(User.create("commenter"));
		Post post = postRepository.save(Post.create(postAuthor, "게시글 제목", "게시글 내용"));

		commentCommandService.addComment(
				post.getId(),
				new CommentCreateRequest(commenter.getId(), "삭제할 댓글")
		);

		entityManager.flush();
		entityManager.clear();

		Long commentId = commentQueryService.getComments(post.getId())
				.get(0)
				.id();

		// when
		commentCommandService.removeComment(post.getId(), commentId, commenter.getId());

		entityManager.flush();
		entityManager.clear();

		List<CommentResponse> comments = commentQueryService.getComments(post.getId());

		// then
		assertTrue(comments.isEmpty());
	}

	@Test
	@DisplayName("좋아요를 누를 수 있다")
	void likePost_success() {
		// given
		User postAuthor = userRepository.save(User.create("post-author"));
		User user = userRepository.save(User.create("like-user"));
		Post post = postRepository.save(Post.create(postAuthor, "게시글 제목", "게시글 내용"));

		// when
		likeCommandService.likePost(post.getId(), user.getId());

		entityManager.flush();
		entityManager.clear();

		long likeCount = likeQueryService.countLikes(post.getId());

		// then
		assertEquals(1L, likeCount);
	}

	@Test
	@DisplayName("좋아요 개수를 확인할 수 있다")
	void countLikes_success() {
		// given
		User postAuthor = userRepository.save(User.create("post-author"));
		User user1 = userRepository.save(User.create("user1"));
		User user2 = userRepository.save(User.create("user2"));
		Post post = postRepository.save(Post.create(postAuthor, "게시글 제목", "게시글 내용"));

		likeCommandService.likePost(post.getId(), user1.getId());
		likeCommandService.likePost(post.getId(), user2.getId());

		entityManager.flush();
		entityManager.clear();

		// when
		long likeCount = likeQueryService.countLikes(post.getId());

		// then
		assertEquals(2L, likeCount);
	}
}