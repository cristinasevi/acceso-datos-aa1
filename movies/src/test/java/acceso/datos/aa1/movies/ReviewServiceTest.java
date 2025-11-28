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
                new Review(1L, "Amazing movie!", 10, LocalDate.of(2025, 11, 27), true, false, null, null),
                new Review(2L, "Great film", 9, LocalDate.of(2025, 11, 28), true, false, null, null)
        );

        List<ReviewOutDto> mockReviewOutDtoList = List.of(
                new ReviewOutDto(1L, "Amazing movie!", 10, LocalDate.of(2025, 11, 27), true, false, "csevi", "Inception"),
                new ReviewOutDto(2L, "Great film", 9, LocalDate.of(2025, 11, 28), true, false, "mdiaz", "The Matrix")
        );

        when(reviewRepository.findAll()).thenReturn(mockReviewList);
        when(modelMapper.map(any(List.class), any(Type.class))).thenReturn(mockReviewOutDtoList);

        List<ReviewOutDto> actualReviewList = reviewService.findAll(null, null, null);

        assertEquals(2, actualReviewList.size());
        assertEquals("Amazing movie!", actualReviewList.getFirst().getComment());
        assertEquals("Great film", actualReviewList.getLast().getComment());

        verify(reviewRepository, times(1)).findAll();
        verify(reviewRepository, times(0)).findByRecommended(true);
    }

    @Test
    public void testFindAllByRecommended() {
        List<Review> mockReviewList = List.of(
                new Review(1L, "Amazing movie!", 10, LocalDate.of(2025, 11, 27), true, false, null, null),
                new Review(2L, "Great film", 9, LocalDate.of(2025, 11, 28), true, false, null, null)
        );

        List<ReviewOutDto> mockReviewOutDtoList = List.of(
                new ReviewOutDto(1L, "Amazing movie!", 10, LocalDate.of(2025, 11, 27), true, false, "csevi", "Inception"),
                new ReviewOutDto(2L, "Great film", 9, LocalDate.of(2025, 11, 28), true, false, "mdiaz", "The Matrix")
        );

        when(reviewRepository.findByRecommended(true)).thenReturn(mockReviewList);
        when(modelMapper.map(any(List.class), any(Type.class))).thenReturn(mockReviewOutDtoList);

        List<ReviewOutDto> actualReviewList = reviewService.findAll(null, true, null);

        assertEquals(2, actualReviewList.size());
        assertEquals("Amazing movie!", actualReviewList.getFirst().getComment());
        assertEquals("Great film", actualReviewList.getLast().getComment());

        verify(reviewRepository, times(0)).findAll();
        verify(reviewRepository, times(1)).findByRecommended(true);
    }
}
