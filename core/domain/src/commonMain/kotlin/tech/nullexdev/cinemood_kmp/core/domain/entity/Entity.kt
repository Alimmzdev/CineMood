package tech.nullexdev.cinemood.core.domain.entity

/** Marker interface for all domain entities */
interface Entity {
    /** All entities should have a unique identifier */
    val id: String
}