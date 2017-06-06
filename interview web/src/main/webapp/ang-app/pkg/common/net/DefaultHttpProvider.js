Package("common.net")
.DefaultHttpProvider = function (responseData) {
	Import("tools.JsonUtils");
    PKG.tools.JsonUtils.convertDateStringsToDates(responseData);
    PKG.tools.JsonUtils.toJsonCycle(responseData);
    return responseData;
};