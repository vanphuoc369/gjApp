package vn.toancauxanh.gg.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSessionCount is a Querydsl query type for SessionCount
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSessionCount extends EntityPathBase<SessionCount> {

    private static final long serialVersionUID = 2084738522L;

    private static final PathInits INITS = new PathInits("*", "nguoiSua.*.*.*.*", "nguoiTao.*.*.*.*");

    public static final QSessionCount sessionCount = new QSessionCount("sessionCount");

    public final vn.toancauxanh.model.QModel _super;

    //inherited
    public final BooleanPath daXoa;

    public final StringPath hostName = createString("hostName");

    //inherited
    public final NumberPath<Long> id;

    public final StringPath ip = createString("ip");

    //inherited
    public final DateTimePath<java.util.Date> ngaySua;

    //inherited
    public final DateTimePath<java.util.Date> ngayTao;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiSua;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiTao;

    public final StringPath sessionId = createString("sessionId");

    //inherited
    public final StringPath trangThai;

    public QSessionCount(String variable) {
        this(SessionCount.class, forVariable(variable), INITS);
    }

    public QSessionCount(Path<? extends SessionCount> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSessionCount(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSessionCount(PathMetadata metadata, PathInits inits) {
        this(SessionCount.class, metadata, inits);
    }

    public QSessionCount(Class<? extends SessionCount> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new vn.toancauxanh.model.QModel(type, metadata, inits);
        this.daXoa = _super.daXoa;
        this.id = _super.id;
        this.ngaySua = _super.ngaySua;
        this.ngayTao = _super.ngayTao;
        this.nguoiSua = _super.nguoiSua;
        this.nguoiTao = _super.nguoiTao;
        this.trangThai = _super.trangThai;
    }

}

