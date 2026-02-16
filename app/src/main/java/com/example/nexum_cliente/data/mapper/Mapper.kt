<<<<<<< Updated upstream
package com.example.nexum_trabajador.data.mapper
=======
package com.example.nexum_cliente.data.mapper
>>>>>>> Stashed changes

/**
 * Interfaz genérica para realizar mapeos entre un objeto de transferencia de datos (DTO) y una Entidad.
 * @param <DTO> El tipo de objeto de datos de entrada (generalmente de una respuesta de red).
 * @param <ENTITY> El tipo de objeto de entidad de salida (generalmente para la base de datos local).
 */
interface EntityMapper<DTO, ENTITY> {
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
    fun toEntity(dto: DTO): ENTITY

    fun toEntity(dtos: List<DTO>): List<ENTITY> = dtos.map { toEntity(it) }
}

/**
 * Interfaz genérica para realizar mapeos entre una Entidad de base de datos y un Modelo de Dominio.
 * @param <ENTITY> El tipo de objeto de entidad de entrada (generalmente de la base de datos local).
 * @param <DOMAIN_MODEL> El tipo de objeto de dominio de salida (para ser usado en la UI).
 */
interface DomainMapper<ENTITY, DOMAIN_MODEL> {
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
    fun toDomain(entity: ENTITY): DOMAIN_MODEL

    fun toDomain(entities: List<ENTITY>): List<DOMAIN_MODEL> = entities.map { toDomain(it) }
}
<<<<<<< Updated upstream
=======


/**
 * Interfaz genérica cuya responsabilidad es mapear de un state de un viewmodel y pasarlo a un request, para hacer una solicitud en una fuente de datos remota.
 * @param <STATE> El tipo de objeto del estado del ViewModel.
 * @param <REQUEST> El tipo de objeto de solicitud para la fuente de datos remota.
 */
interface RequestMapper<STATE, REQUEST> {
    fun toRequest(state: STATE): REQUEST
    fun toRequest(states: List<STATE>): List<REQUEST> = states.map { toRequest(it) }
}


interface StateMapper<STATE, DOMAIN_MODEL> {
    fun toState(domainModel: DOMAIN_MODEL): STATE
    fun toState(domainModels: List<DOMAIN_MODEL>): List<STATE> = domainModels.map {
        toState(it)
    }
}

/**
 * Interfaz genérica para mapear desde un State de ViewModel a un Modelo de Dominio.
 * @param <STATE> El tipo de objeto de estado de entrada (del ViewModel).
 * @param <DOMAIN_MODEL> El tipo de objeto de dominio de salida (para ser usado en los casos de uso).
 */
interface StateToDomainMapper<STATE, DOMAIN_MODEL> {
    fun stateToDomain(state: STATE): DOMAIN_MODEL
    fun stateToDomain(states: List<STATE>): List<DOMAIN_MODEL> = states.map { stateToDomain(it) }
}
>>>>>>> Stashed changes
