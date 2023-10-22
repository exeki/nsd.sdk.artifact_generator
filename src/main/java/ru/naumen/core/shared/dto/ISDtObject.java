package ru.naumen.core.shared.dto;

import ru.naumen.core.shared.*;
import ru.naumen.metainfo.shared.IClassFqn;

import javax.annotation.CheckForNull;

public interface ISDtObject extends IUUIDIdentifiable, ISTitled, HasAttrPermissions, ISHasMetainfo, HasPermissionMetaData {
    IClassFqn getMetainfo();

    @CheckForNull
    Boolean hasAuthAttrPermission(String var1, boolean var2);

    @CheckForNull
    Boolean hasPermission(String var1);
}
