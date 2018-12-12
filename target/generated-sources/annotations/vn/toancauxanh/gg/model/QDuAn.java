package vn.toancauxanh.gg.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDuAn is a Querydsl query type for DuAn
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDuAn extends EntityPathBase<DuAn> {

    private static final long serialVersionUID = -1329066145L;

    private static final PathInits INITS = new PathInits("*", "nguoiSua.*.*.*.*", "nguoiTao.*.*.*.*");

    public static final QDuAn duAn = new QDuAn("duAn");

    public final vn.toancauxanh.model.QModel _super;

    //inherited
    public final BooleanPath daXoa;

    //inherited
    public final NumberPath<Long> id;

    //inherited
    public final DateTimePath<java.util.Date> ngaySua;

    //inherited
    public final DateTimePath<java.util.Date> ngayTao;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiSua;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiTao;

    public final StringPath stringListId = createString("stringListId");

    public final StringPath tenDuAn = createString("tenDuAn");

    public final StringPath tomTatNoiDung = createString("tomTatNoiDung");

    //inherited
    public final StringPath trangThai;

    public QDuAn(String variable) {
        this(DuAn.class, forVariable(variable), INITS);
    }

    public QDuAn(Path<? extends DuAn> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDuAn(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDuAn(PathMetadata metadata, PathInits inits) {
        this(DuAn.class, metadata, inits);
    }

    public QDuAn(Class<? extends DuAn> type, PathMetadata metadata, PathInits inits) {
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

