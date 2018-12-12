package vn.toancauxanh.gg.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QImage is a Querydsl query type for Image
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QImage extends EntityPathBase<Image> {

    private static final long serialVersionUID = 1753032378L;

    private static final PathInits INITS = new PathInits("*", "author.*.*.*.*", "nguoiSua.*.*.*.*", "nguoiTao.*.*.*.*");

    public static final QImage image = new QImage("image");

    public final QAsset _super;

    public final BooleanPath articlesImage = createBoolean("articlesImage");

    public final vn.toancauxanh.model.QNhanVien author;

    //inherited
    public final BooleanPath ckEditorPopup;

    //inherited
    public final BooleanPath daXoa;

    public final StringPath description = createString("description");

    public final StringPath extension = createString("extension");

    public final NumberPath<Integer> height = createNumber("height", Integer.class);

    //inherited
    public final NumberPath<Long> id;

    public final StringPath imageUrl = createString("imageUrl");

    public final StringPath medium = createString("medium");

    public final NumberPath<Double> money = createNumber("money", Double.class);

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.util.Date> ngaySua;

    //inherited
    public final DateTimePath<java.util.Date> ngayTao;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiSua;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiTao;

    public final BooleanPath publishStatus = createBoolean("publishStatus");

    public final StringPath small = createString("small");

    public final StringPath title = createString("title");

    //inherited
    public final StringPath trangThai;

    //inherited
    public final StringPath trangThaiSoan;

    public final NumberPath<Integer> width = createNumber("width", Integer.class);

    public QImage(String variable) {
        this(Image.class, forVariable(variable), INITS);
    }

    public QImage(Path<? extends Image> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QImage(PathMetadata metadata, PathInits inits) {
        this(Image.class, metadata, inits);
    }

    public QImage(Class<? extends Image> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QAsset(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new vn.toancauxanh.model.QNhanVien(forProperty("author"), inits.get("author")) : null;
        this.ckEditorPopup = _super.ckEditorPopup;
        this.daXoa = _super.daXoa;
        this.id = _super.id;
        this.ngaySua = _super.ngaySua;
        this.ngayTao = _super.ngayTao;
        this.nguoiSua = _super.nguoiSua;
        this.nguoiTao = _super.nguoiTao;
        this.trangThai = _super.trangThai;
        this.trangThaiSoan = _super.trangThaiSoan;
    }

}

