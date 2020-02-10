package com.cac.service.infrastructure

import com.cac.service.infrastructure.view.ExerciseView
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.UUID

interface ExerciseRepository: MongoRepository<ExerciseView, UUID>