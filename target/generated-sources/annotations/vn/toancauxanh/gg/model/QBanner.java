package vn.toancauxanh.gg.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBanner is a Querydsl query type for Banner
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBanner extends EntityPathBase<Banner> {

    private static final long serialVersionUID = -1701663315L;

    private static final PathInits INITS = new PathInits("*", "nguoiSua.*.*.*.*", "nguoiTao.*.*.*.*");

    public static final QBanner banner = new QBanner("banner");

    public final vn.toancauxanh.model.QModel _super;

    public final StringPath alt = createString("alt");

    public final StringPath bannerLink = createString("bannerLink");

    public final StringPath bannerUrl = createString("bannerUrl");

    public final NumberPath<Long> clickCount = createNumber("clickCount", Long.class);

    //inherited
    public final BooleanPath daXoa;

    public final StringPath description = createString("description");

    public final StringPath extension = createString("extension");

    public final NumberPath<Integer> height = createNumber("height", Integer.class);

    //inherited
    public final NumberPath<Long> id;

    public final StringPath medium = createString("medium");

    public final StringPath name = createString("name");

    public final BooleanPath newTab = createBoolean("newTab");

    public final DateTimePath<java.util.Date> ngayBatDau = createDateTime("ngayBatDau", java.util.Date.class);

    public final DateTimePath<java.util.Date> ngayHetHan = createDateTime("ngayHetHan", java.util.Date.class);

    //inherited
    public final DateTimePath<java.util.Date> ngaySua;

    //inherited
    public final DateTimePath<java.util.Date> ngayTao;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiSua;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiTao;

    public final StringPath small = createString("small");

    public final NumberPath<Integer> soThuTu = createNumber("soThuTu", Integer.class);

    public final StringPath title = createString("title");

    //inherited
    public final StringPath trangThai;

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public final NumberPath<Integer> width = createNumber("width", Integer.class);

    public QBanner(String variable) {
        this(Banner.class, forVariable(variable), INITS);
    }

    public QBanner(Path<? extends Banner> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBanner(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBanner(PathMetadata metadata, PathInits inits) {
        this(Banner.class, metadata, inits);
    }

    public QBanner(Class<? extends Banner> type, PathMetadata metadata, PathInits inits) {
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

