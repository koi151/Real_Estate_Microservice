package com.koi151.msproperties.repository;

import com.koi151.msproperties.entity.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Rooms, Integer> {
}
