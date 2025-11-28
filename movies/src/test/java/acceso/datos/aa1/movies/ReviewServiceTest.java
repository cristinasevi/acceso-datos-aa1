package acceso.datos.aa1.movies;

import acceso.datos.aa1.movies.domain.Review;
import acceso.datos.aa1.movies.dto.ReviewOutDto;
import acceso.datos.aa1.movies.repository.ReviewRepository;
import acceso.datos.aa1.movies.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testFindAll() {
        List<Review> mockReviewList = List.of(
                createMockReview(1, "Amazing movie!", 10),
                createMockReview(2, "Great film", 9)
        );

        List<ReviewOutDto> mockReviewOutDtoList = List.of(
                new ReviewOutDto(1L, "Amazing movie!", 10, LocalDate.of(2025, 11, 27),
                        true, false, "csevi", "Inception"),
                new ReviewOutDto(2L, "Great film", 9, LocalDate.of(2025, 11, 28),
                        true, false, "mdiaz", "The Matrix")
        );

        when(reviewRepository.findAll()).thenReturn(mockReviewList);
        when(modelMapper.map(any(List.class), any(Type.class))).thenReturn(mockReviewOutDtoList);

        List<ReviewOutDto> actualReviewList = reviewService.findAll();

        assertEquals(2, actualReviewList.size());
        assertEquals("Amazing movie!", actualReviewList.get(0).getComment());
        assertEquals("Great film", actualReviewList.get(1).getComment());

        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    public void testFindByMovieId() {
        List<Review> mockReviewList = List.of(
                createMockReview(1, "Amazing movie!", 10),
                createMockReview(2, "Mind-blowing", 10)
        );

        List<ReviewOutDto> mockReviewOutDtoList = List.of(
                new ReviewOutDto(1L, "Amazing movie!", 10, LocalDate.of(2025, 11, 27),
                        true, false, "csevi", "Inception"),
                new ReviewOutDto(2L, "Mind-blowing", 10, LocalDate.of(2025, 11, 28),
                        true, false, "mdiaz", "Inception")
        );

        when(reviewRepository.findByMovieId(1L)).thenReturn(mockReviewList);
        when(modelMapper.map(any(List.class), any(Type.class))).thenReturn(mockReviewOutDtoList);

        List<ReviewOutDto> actualReviewList = reviewService.findByMovieId(1L);

        assertEquals(2, actualReviewList.size());
        assertEquals("Inception", actualReviewList.get(0).getMovieTitle());
        assertEquals("Inception", actualReviewList.get(1).getMovieTitle());

        verify(reviewRepository, times(1)).findByMovieId(1L);
    }

    @Test
    public void testFindByUserId() {
        List<Review> mockReviewList = List.of(
                createMockReview(1, "Amazing movie!", 10),
                createMockReview(2, "Great soundtrack", 9)
        );

        List<ReviewOutDto> mockReviewOutDtoList = List.of(
                new ReviewOutDto(1L, "Amazing movie!", 10, LocalDate.of(2025, 11, 27),
                        true, false, "csevi", "Inception"),
                new ReviewOutDto(2L, "Great soundtrack", 9, LocalDate.of(2025, 11, 28),
                        true, false, "csevi", "Interstellar")
        );

        when(reviewRepository.findByUserId(1L)).thenReturn(mockReviewList);
        when(modelMapper.map(any(List.class), any(Type.class))).thenReturn(mockReviewOutDtoList);

        List<ReviewOutDto> actualReviewList = reviewService.findByUserId(1L);

        assertEquals(2, actualReviewList.size());
        assertEquals("csevi", actualReviewList.get(0).getUsername());
        assertEquals("csevi", actualReviewList.get(1).getUsername());

        verify(reviewRepository, times(1)).findByUserId(1L);
    }

    private Review createMockReview(long id, String comment, int rating) {
        Review review = new Review();
        review.setId(id);
        review.setComment(comment);
        review.setRating(rating);
        review.setReviewDate(LocalDate.now());
        review.setRecommended(true);
        review.setSpoiler(false);
        return review;
    }
}
