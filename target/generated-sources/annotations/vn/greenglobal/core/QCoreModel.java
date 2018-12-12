package vn.greenglobal.core;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QCoreModel is a Querydsl query type for CoreModel
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QCoreModel extends EntityPathBase<CoreModel<?>> {

    private static final long serialVersionUID = -291181927L;

    public static final QCoreModel coreModel = new QCoreModel("coreModel");

    public final BooleanPath daXoa = createBoolean("daXoa");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.util.Date> ngaySua = createDateTime("ngaySua", java.util.Date.class);

    public final DateTimePath<java.util.Date> ngayTao = createDateTime("ngayTao", java.util.Date.class);

    public final EnumPath<StatusType> trangThai = createEnum("trangThai", StatusType.class);

    @SuppressWarnings({"all", "rawtypes", "unchecked"})
    public QCoreModel(String variable) {
        super((Class) CoreModel.class, forVariable(variable));
    }

    @SuppressWarnings({"all", "rawtypes", "unchecked"})
    public QCoreModel(Path<? extends CoreModel> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    @SuppressWarnings({"all", "rawtypes", "unchecked"})
    public QCoreModel(PathMetadata metadata) {
        super((Class) CoreModel.class, metadata);
    }

}

