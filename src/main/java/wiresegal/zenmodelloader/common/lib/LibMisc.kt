package wiresegal.zenmodelloader.common.lib

object LibMisc {
    const val MOD_NAME = "Zen Model Loader"
    const val MOD_ID = "zml"

    const val BUILD = "GRADLE:BUILD"
    const val VERSIONID = "GRADLE:VERSION"
    const val VERSION = "$VERSIONID.$BUILD"
    const val DEPENDENCIES = "after:*"

    const val VERSIONS = "[1.10,1.10.2]"

    const val PROXY_COMMON = "wiresegal.zenmodelloader.common.core.CommonProxy"
    const val PROXY_CLIENT = "wiresegal.zenmodelloader.client.core.ClientProxy"
}
