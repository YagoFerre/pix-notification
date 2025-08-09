package yago.ferreira.api.domain.common;

public interface BaseMapper<Entity, Model, Request, Response> {
    Entity toEntity(Model model);
    Model toDomainModel(Entity entity);
    Response modelToResponse(Model model);
    Model requestToModel(Request request);
}
