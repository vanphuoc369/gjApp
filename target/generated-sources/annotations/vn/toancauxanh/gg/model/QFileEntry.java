package vn.toancauxanh.gg.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFileEntry is a Querydsl query type for FileEntry
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFileEntry extends EntityPathBase<FileEntry> {

    private static final long serialVersionUID = -397351371L;

    private static final PathInits INITS = new PathInits("*", "nguoiSua.*.*.*.*", "nguoiTao.*.*.*.*");

    public static final QFileEntry fileEntry = new QFileEntry("fileEntry");

    public final vn.toancauxanh.model.QModel _super;

    //inherited
    public final BooleanPath daXoa;

    public final StringPath description = createString("description");

    public final StringPath extension = createString("extension");

    public final StringPath fileUrl = createString("fileUrl");

    //inherited
    public final NumberPath<Long> id;

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.util.Date> ngaySua;

    //inherited
    public final DateTimePath<java.util.Date> ngayTao;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiSua;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiTao;

    public final StringPath tenHienThi = createString("tenHienThi");

    public final StringPath tepDinhKem = createString("tepDinhKem");

    public final StringPath title = createString("title");

    //inherited
    public final StringPath trangThai;

    public QFileEntry(String variable) {
        this(FileEntry.class, forVariable(variable), INITS);
    }

    public QFileEntry(Path<? extends FileEntry> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFileEntry(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFileEntry(PathMetadata metadata, PathInits inits) {
        this(FileEntry.class, metadata, inits);
    }

    public QFileEntry(Class<? extends FileEntry> type, PathMetadata metadata, PathInits inits) {
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

