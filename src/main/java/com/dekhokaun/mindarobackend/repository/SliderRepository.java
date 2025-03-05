package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.Slider;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SliderRepository extends BaseRepository<Slider, Integer> {

    List<Slider> findByStatus(Integer status);

    List<Slider> findByCode(Integer code);
}
