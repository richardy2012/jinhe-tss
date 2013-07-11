    /*
     *	后台响应数据节点名称
     */
    XML_MAIN_TREE = "GroupTree";
    XML_USER_LIST = "SourceList";
    XML_GRID_SEARCH = "GridSearch";
    XML_SEARCH_USER = "SearchUserList";
    XML_OPERATION = "Operation";
    XML_PAGE_LIST = "PageList";

    XML_USER_INFO = "UserInfo";
    XML_AUTHENTICATE_INFO = "AuthenticateInfo";
    XML_USER_TO_GROUP_TREE = "User2GroupTree";
    XML_USER_TO_GROUP_EXIST_TREE = "User2GroupExistTree";
    XML_USER_TO_ROLE_TREE = "User2RoleTree";
    XML_USER_TO_ROLE_EXIST_TREE = "User2RoleExistTree";

    XML_GROUP_INFO = "GroupInfo";
    XML_GROUP_TO_USER_TREE = "Group2UserTree";
    XML_GROUP_TO_USER_LIST_TREE = "Group2UserListTree";
    XML_GROUP_TO_USER_EXIST_TREE = "Group2UserExistTree";
    XML_GROUP_TO_ROLE_TREE = "Group2RoleTree";
    XML_GROUP_TO_ROLE_EXIST_TREE = "Group2RoleExistTree";

    XML_AUTO_MAPPING = "AutoMapping";
    XML_MANUAL_MAPPING = "ManualMapping";
    XML_SEARCH_MANUAL_MAPPING = "SearchManualMapping";

    XML_GENERAL_SEARCH_MAPPING = "GeneralSearchMappingGrid";
    XML_GENERAL_SEARCH_SYNC = "GeneralSearchSyncGrid";
    XML_GENERAL_SEARCH_PERMISSION = "GeneralSearchPermissionInfo";
    XML_GENERAL_SEARCH_PERMISSION_LIST = "GeneralSearchPermissionList";
    XML_GENERAL_SEARCH_REASSIGN = "GeneralSearchUserStrategyInfoGrid";
    XML_GENERAL_SEARCH_ROLE = "GeneralSearchRoleGrid";

    XML_RESOURCE_TYPE = "ResourceType";
    XML_APPLICATION_DETAIL = "AppDetail";

    XML_SECURITY_LEVEL = "SecurityLevel";
    /*
     *	默认唯一编号名前缀
     */
    CACHE_GRID_ROW_DETAIL = "row__id";
    CACHE_VIEW_GRID_ROW_DETAIL = "viewRow__id";
    CACHE_TREE_NODE_DETAIL = "treeNode__id";
    CACHE_VIEW_TREE_NODE_DETAIL = "viewTreeNode__id";
    CACHE_TREE_NODE_GRID = "treeNodeGrid__id";
    CACHE_MAIN_TREE = "tree__id";
    CACHE_TOOLBAR = "toolbar__id";
    CACHE_AUTO_MAPPING = "autoMapping__id";
    CACHE_MANUAL_MAPPING = "manualMapping__id";
    CACHE_GROUP_TO_USER_GRID = "group2User__id";
    CACHE_SEARCH_USER = "searchUser__id";
    CACHE_GENERAL_SEARCH_MAPPING = "generalSearchMapping__id";
    CACHE_GENERAL_SEARCH_SYNC = "generalSearchSync__id";
    CACHE_GENERAL_SEARCH_PERMISSION = "generalSearchPermission__id";
    CACHE_GENERAL_SEARCH_PERMISSION_LIST = "generalSearchPermissionList__id";
    CACHE_GENERAL_SEARCH_REASSIGN = "generalSearchReassign__id";
    CACHE_GENERAL_SEARCH_ROLE = "generalSearchRole__id";
    CACHE_APPLICATION_DETAIL = "app__id";
    CACHE_VIEW_APPLICATION_DETAIL = "viewApp__id";
    /*
     *	名称
     */
    OPERATION_ADD = "新建$label";
    OPERATION_VIEW = "查看\"$label\"";
    OPERATION_DEL = "删除\"$label\"";
    OPERATION_EDIT = "编辑\"$label\"";
    OPERATION_AUTO_MAPPING = "模糊对应\"$label\"";
    OPERATION_MANUAL_MAPPING = "手工对应\"$label\"";
    OPERATION_SEARCH = "查询\"$label\"";
    /*
     *	XMLHTTP请求地址汇总
     */
    URL_INIT = "data/group_init.xml";
    URL_USER_LIST = "data/grid.xml";
    URL_USER_DETAIL = "data/user1.xml";
    URL_GROUP_DETAIL = "data/group1.xml";
    URL_USER_SEARCH = "data/gridsearch.xml";
    URL_SAVE_USER = "data/_success.xml";
    URL_SAVE_GROUP = "data/_success.xml";
    URL_GROUP_TO_USER_SEARCH = "data/usersearch.xml";
    URL_DEL_GROUP = "data/_success.xml";
    URL_MOVE_USER = "data/_success.xml";
    URL_MOVE_GROUP = "data/_success.xml";
    URL_START_GROUP = "data/_success.xml";
    URL_STOP_GROUP = "data/_success.xml";
    URL_SORT_GROUP = "data/_success.xml";
    URL_STOP_USER = "data/_success.xml";
    URL_START_USER = "data/_success.xml";
    URL_SORT_USER = "data/_success.xml";
    URL_SORT_USER_CROSS_PAGE = "data/_success.xml";
    URL_SYNC_GROUP = "data/progress.xml";
    URL_SYNC_PROGRESS = "data/progress.xml";
    URL_CANCEL_SYNC_PROGRESS = "data/_success.xml";
    URL_COPY_GROUP = "data/_success.xml";
    URL_AUTO_MAPPING = "data/automapping1.xml";
    URL_SAVE_AUTO_MAPPING = "data/_success.xml";
    URL_MANUAL_MAPPING = "data/manualmapping1.xml";
    URL_SAVE_MANUAL_MAPPING = "data/_success.xml";
    URL_SEARCH_MANUAL_MAPPING = "data/searchmanualmapping1.xml";
    URL_GROUP_TO_USER_LIST = "data/userlist.xml";
    URL_DEL_USER = "data/_success.xml";
    URL_SEARCH_USER = "data/grid.xml";
    URL_RESET_PASSWORD = "data/_success.xml";
    URL_GET_OPERATION = "data/operation.xml";
    URL_GET_USER_OPERATION = "data/operation.xml";
    URL_SET_AUTHENTICATE_METHOD = "data/_success.xml";
    URL_GENERAL_SEARCH_MAPPING = "data/group_general_search.xml";
    URL_GENERAL_SEARCH_SYNC = "data/group_general_search.xml";
    URL_GENERAL_SEARCH_PERMISSION = "data/group_general_search.xml";
    URL_GENERAL_SEARCH_GET_RESOURCETYPES = "data/resourcetype.xml";
    URL_GENERAL_SEARCH_PERMISSION_LIST = "data/group_general_search.xml";
    URL_GENERAL_SEARCH_REASSIGN = "data/group_general_search.xml";
    URL_GENERAL_SEARCH_ROLE = "data/group_general_search.xml";
    URL_AUTHENTICATE_GROUP = "data/_success.xml";
    URL_APPLICATION_DETAIL = "data/application.xml";
    URL_SAVE_APPLICATION = "data/_success.xml";
    URL_DEL_APPLICATION = "data/_success.xml";
    URL_SORT_APPLICATION = "data/_success.xml";
    URL_SYNC_USER = "data/_success.xml";
	URL_IMPORT_GROUP = "data/progress.xml";
    URL_IMPORT_PROGRESS = "data/progress.xml";
    URL_CANCEL_IMPORT_PROGRESS = "data/_success.xml";
    URL_CHECK_PASSWORD = "data/password_check.xml";
    URL_CHECK_GROUP_PASSWORD = "data/password_check.xml";
    URL_SET_GROUP_PASSWORD_TACTIC = "data/_success.xml";
    URL_SET_USER_PASSWORD_TACTIC = "data/_success.xml";

    URL_INIT = "ums/group!getAllGroup2Tree.action";
    URL_USER_LIST = "ums/user!getUsersByGroupId.action";
    URL_USER_DETAIL = "ums/user!getUserInfoAndRelation.action";
    URL_GROUP_DETAIL = "ums/group!getGroupInfoAndRelation.action";
    URL_USER_SEARCH = "data/gridsearch.xml";
    URL_SAVE_USER = "ums/user!saveUser.action";
    URL_SAVE_GROUP = "ums/group!editGroup.action";
    URL_GROUP_TO_USER_SEARCH = "data/usersearch.xml";
    URL_DEL_GROUP = "ums/group!deleteGroup.action";
    URL_MOVE_USER = "ums/user!moveUser.action";
    URL_MOVE_GROUP = "ums/group!moveGroup.action";
    URL_START_GROUP = "ums/group!startOrStopGroup.action";
    URL_STOP_GROUP = "ums/group!startOrStopGroup.action";
    URL_SORT_GROUP = "ums/group!sortGroup.action";
    URL_STOP_USER = "ums/user!startOrStopUser.action";
    URL_START_USER = "ums/user!startOrStopUser.action";
    URL_SORT_USER = "ums/user!sortUser.action";
    URL_SORT_USER_CROSS_PAGE = "ums/user!sortUserCrossPage.action";
    URL_SYNC_GROUP = "ums/syncdata!syncData.action";
    URL_SYNC_PROGRESS = "ums/syncdata!getProgress.action";
    URL_CANCEL_SYNC_PROGRESS = "ums/syncdata!doConceal.action";
    URL_COPY_GROUP = "ums/group!copyGroup.action";
    URL_AUTO_MAPPING = "ums/user!getAutoMappingInfo.action";
    URL_SAVE_AUTO_MAPPING = "ums/user!editAutoMappingInfo.action";
    URL_MANUAL_MAPPING = "ums/user!getManualMappingInfo.action";
    URL_SAVE_MANUAL_MAPPING = "ums/user!editManualMappingInfo.action";
    URL_SEARCH_MANUAL_MAPPING = "ums/user!searchMappingUser.action";
    URL_GROUP_TO_USER_LIST = "ums/user!getSelectedUsersByGroupId.action";
    URL_DEL_USER = "ums/user!deleteUser.action";
    URL_SEARCH_USER = "ums/user!searchUser.action";
    URL_RESET_PASSWORD = "ums/user!initPassword.action";
    URL_GET_OPERATION = "ums/group!getOperation.action";
    URL_GET_USER_OPERATION = "ums/user!getOperation.action";
    URL_SET_AUTHENTICATE_METHOD = "ums/user!uniteAuthenticateMethod.action";
    URL_GENERAL_SEARCH_MAPPING = "ums/generalSearch!searchMapping.action";
    URL_GENERAL_SEARCH_SYNC = "ums/generalSearch!searchUsersByGroup.action";
    URL_GENERAL_SEARCH_PERMISSION = "ums/generalSearch!getAllApplications.action";
    URL_GENERAL_SEARCH_GET_RESOURCETYPES = "ums/generalSearch!getResourceTypes.action";
    URL_GENERAL_SEARCH_PERMISSION_LIST = "ums/generalSearch!searchPermission.action";
    URL_GENERAL_SEARCH_REASSIGN = "ums/generalSearch!searchUserStrategyInfo.action";
    URL_GENERAL_SEARCH_ROLE = "ums/generalSearch!searchRolesInfo.action";
    URL_AUTHENTICATE_GROUP = "data/_success.xml";
    URL_APPLICATION_DETAIL = "ums/appResource!getApplicationInfo.action";
    URL_SAVE_APPLICATION = "ums/appResource!editApplication.action";
    URL_DEL_APPLICATION = "ums/appResource!deleteApplication.action";
    URL_SORT_APPLICATION = "ums/appResource!sortApplication.action";
    URL_SYNC_USER = "ums/syncdata!syncUser.action";
    URL_IMPORT_GROUP = "ums/group!importGroup.action";
	URL_IMPORT_USER = "ums/user!importUser.action";
    URL_IMPORT_PROGRESS = "ums/group!getProgress.action";
    URL_CANCEL_IMPORT_PROGRESS = "ums/group!doConceal.action";
    URL_CHECK_PASSWORD = "ums/passwordrule!getStrengthLevel.action";
    URL_CHECK_GROUP_PASSWORD = "ums/passwordrule!getGroupStrengthLevel.action";
    URL_SET_GROUP_PASSWORD_TACTIC = "ums/group!setPasswordRule.action";
    URL_SET_USER_PASSWORD_TACTIC = "ums/user!setPasswordRule.action";
 
    /*
     *	icon路径
     */
    ICON = "../platform/images/icon/";
 
    function init(){
        initPaletteResize();
        initListContainerResize();
        initToolBar();
        initNaviBar("ums.1");
        initMenus();
        initBlocks();
        initWorkSpace();
        initEvents();
        initFocus();

        loadInitData();
    }

    function loadInitData(){
        var p = new HttpRequestParams();
        p.url = URL_INIT;

        var request = new HttpRequest(p);
        request.onresult = function(){
            var _operation = this.getNodeValue(XML_OPERATION);

            var groupTreeNode = this.getNodeValue(XML_MAIN_TREE);
            var groupTreeNodeID = CACHE_MAIN_TREE;

            Cache.XmlIslands.add(groupTreeNodeID,groupTreeNode);

            loadToolBar(_operation);
            initTree(groupTreeNodeID);
        }
        request.send();
    }

    function initMenus(){
        initTreeMenu();
        initGridMenu();
    }

    function initTreeMenu(){
        var item1 = {
            label:"停用",
            callback:stopGroup,
            icon:ICON + "stop.gif",
            enable:function(){return true;},
            visible:function(){return true!=isRootNode() && true==getOperation("7t") && "0"==getGroupState();}
        }
        var item2 = {
            label:"启用",
            callback:startGroup,
            icon:ICON + "start.gif",
            enable:function(){return true;},
            visible:function(){return true!=isRootNode() && true==getOperation("7") && "1"==getGroupState();}
        }
        var item3 = {
            label:"编辑",
            callback:editTreeNode,
            icon:ICON + "edit.gif",
            enable:function(){return true;},
            visible:function(){return true!=isRootNode() && "3"!=getGroupType() && true==getOperation("4");}
        }
        var item4 = {
            label:"删除",
            callback:delTreeNode,
            icon:ICON + "del.gif",
            enable:function(){return true;},
            visible:function(){return true!=isRootNode() && "3"!=getGroupType() && true==getOperation("3");}
        }
 
        var item8 = {
            label:"新建用户组",
            callback:addNewGroup,
            enable:function(){return true;},
            visible:function(){return true!=isSelfRegisterNode() && "3"!=getGroupType() && true!=isOtherGroup() && true==getOperation("2");}
        }
        var item9 = {
            label:"新建用户",
            callback:addNewUser,
            enable:function(){return true;},
            visible:function(){return true!=isRootNode() && "5"!=getResourceTypeId() && "3"!=getGroupType() && true==getOperation("1");}
        }
        var item10 = {
            label:"浏览用户",
            callback:showUserList,
            icon:ICON + "view_list.gif",
            enable:function(){return true;},
            visible:function(){return null==getAppType() && "5"!=getResourceTypeId() && true==getOperation("6")&& (true!=isRootNode() || '-8'==getTreeId() || '-9'==getTreeId())}
        }
        var item11 = {
            label:"搜索用户...",
            callback:searchUser,
            icon:ICON + "search.gif",
            enable:function(){return true;},
            visible:function(){return "5"!=getResourceTypeId() && true==getOperation("10");}
        }


        var item12 = {
            label:"高级功能",
            callback:null,
            enable:function(){return true;},
            visible:function(){return true!=isRootNode() && "5"!=getResourceTypeId();}
        }

        //高级功能
        var subitem12_1 = {
            label:"初始化密码...",
            callback:resetPassword,
            icon:ICON + "init_password.gif",
            enable:function(){return true;},
            visible:function(){return null==getAppType() && true!=isRootNode() && true==getOperation("11");}
        }
 
        var subitem12_3 = {
            label:"设置认证方式...",
            callback:setAuthenticateMethod,
            enable:function(){return true;},
            visible:function(){return true==getOperation("12");}
        }


        var subitem12_4 = {
            label:"综合查询",
            callback:null,
            icon:ICON + "search.gif",
            enable:function(){return true;},
            visible:function(){return true==getOperation("ug16");}
        }

        // 综合查询
        var subitem12_4_3 = {
            label:"用户权限",
            callback:generalSearchPermission,
            enable:function(){return true;},
            visible:function(){return true;}
        }
        var subitem12_4_4 = {
            label:"用户转授",
            callback:generalSearchReassign,
            enable:function(){return true;},
            visible:function(){return true;}
        }
        var subitem12_4_5 = {
            label:"用户角色",
            callback:generalSearchRole,
            enable:function(){return true;},
            visible:function(){return true;}
        }

        var submenu12_4 = new Menu();
        submenu12_4.addItem(subitem12_4_3);
        submenu12_4.addItem(subitem12_4_4);
        submenu12_4.addItem(subitem12_4_5);
        subitem12_4.submenu = submenu12_4;


        var submenu12 = new Menu();
        submenu12.addItem(subitem12_1);
        submenu12.addItem(subitem12_2);
        submenu12.addItem(subitem12_3);
        submenu12.addSeparator();
        submenu12.addItem(subitem12_4);
        item12.submenu = submenu12;





        var item18 = {
            label:"用户同步",
            callback:null,
            icon:ICON + "sync.gif",
            enable:function(){return true;},
            visible:function(){return true==getOperation("15");}
        }
        var submenu18_1 = {
            label:"完全同步",
            callback:function(){syncGroup("1")},
            enable:function(){return true;},
            visible:function(){return true;}
        }
        var submenu18_2 = {
            label:"单向同步",
            callback:function(){syncGroup("2")},
            enable:function(){return true;},
            visible:function(){return true;}
        }
        var submenu18 = new Menu();
        submenu18.addItem(submenu18_1);
        submenu18.addItem(submenu18_2);
        item18.submenu = submenu18;
 

        var item13 = {
            label:"查看",
            callback:function(){
                editTreeNode(false);
            },
            icon:ICON + "view.gif",
            enable:function(){return true;},
            visible:function(){return true!=isRootNode() && "3"!=getGroupType() && true==getOperation("5");}
        }
        var item14 = {
            label:"授予角色",
            callback:setGroupPermission,
            enable:function(){return true;},
            visible:function(){return true!=isRootNode() && true==getOperation("_1");}
        }
        var item15 = {
            label:"新建应用",
            callback:addApplication,
            enable:function(){return true;},
            visible:function(){return "-4"==getTreeId() && true==getOperation("2");}
        }
        var item16 = {
            label:"用户导入到...",
            callback:importGroupTo,
            icon:ICON + "import_user.gif",
            enable:function(){return true;},
            visible:function(){return true!=isRootNode() && "5"!=getResourceTypeId() && "3"==getGroupType();}
        }


        var item17 = {
            label:"维护...",
            callback:null,
            enable:function(){return true;},
            visible:function(){return true!=isRootNode() && "3"==getGroupType();}
        }
        var subitem17_1 = {
            label:"查看",
            callback:function(){
                editTreeNode(false);
            },
            icon:ICON + "view.gif",
            enable:function(){return true;},
            visible:function(){return true!=isRootNode() && true==getOperation("5");}
        }
        var subitem17_2 = {
            label:"编辑",
            callback:editTreeNode,
            icon:ICON + "edit.gif",
            enable:function(){return true;},
            visible:function(){return true!=isRootNode() && true==getOperation("4");}
        }
        var subitem17_3 = {
            label:"删除",
            callback:delTreeNode,
            icon:ICON + "del.gif",
            enable:function(){return true;},
            visible:function(){return true!=isRootNode() && true==getOperation("3");}
        }
        var subitem17_4 = {
            label:"复制",
            callback:copyGroup,
            icon:ICON + "copy.gif",
            enable:function(){return true;},
            visible:function(){return true!=isRootNode() && "5"!=getResourceTypeId() && true==getOperation("13");}
        }
        var subitem17_5 = {
            label:"复制到...",
            callback:copyGroupTo,
            icon:ICON + "copy_to.gif",
            enable:function(){return true;},
            visible:function(){return true!=isRootNode() && "5"!=getResourceTypeId();}
        }
        var subitem17_6 = {
            label:"移动到...",
            callback:moveGroupTo,
            icon:ICON + "move.gif",
            enable:function(){return true;},
            visible:function(){return true!=isRootNode() && "5"!=getResourceTypeId() && true==getOperation("3");}
        }
        var subitem17_7 = {
            label:"新建用户组",
            callback:addNewGroup,
            enable:function(){return true;},
            visible:function(){return true!=isSelfRegisterNode() && true==getOperation("2") && true!=isOtherGroup();}
        }
        var subitem17_8 = {
            label:"新建用户",
            callback:addNewUser,
            enable:function(){return true;},
            visible:function(){return true!=isRootNode() && "5"!=getResourceTypeId() && true==getOperation("1");}
        }
        var submenu17 = new Menu();
        submenu17.addItem(subitem17_1);
        submenu17.addItem(subitem17_2);
        submenu17.addItem(subitem17_3);
        submenu17.addItem(subitem17_4);
        submenu17.addItem(subitem17_5);
        submenu17.addItem(subitem17_6);
        submenu17.addSeparator();
        submenu17.addItem(subitem17_7);
        submenu17.addItem(subitem17_8);
        item17.submenu = submenu17;





        var menu1 = new Menu();
        menu1.addItem(item1);
        menu1.addItem(item2);
        menu1.addSeparator();
        menu1.addItem(item14);
        menu1.addSeparator();

        menu1.addItem(item16);
        menu1.addItem(item18);
        menu1.addItem(item19);


        menu1.addSeparator();
        menu1.addItem(item17);
        menu1.addSeparator();
        menu1.addItem(item13);
        menu1.addItem(item3);
        menu1.addItem(item4);
        menu1.addSeparator();
        menu1.addItem(item8);
        menu1.addItem(item9);
        menu1.addItem(item15);
        menu1.addSeparator();
        menu1.addItem(item10);
        menu1.addItem(item11);
        menu1.addSeparator();
        menu1.addItem(item12);

        var treeObj = $("tree");
        treeObj.contextmenu = menu1;
    }
 
    function initGridMenu(){
        var gridObj = $("grid");
        var item1 = {
            label:"停用",
            callback:stopUser,
            icon:ICON + "stop.gif",
            enable:function(){return true;},
            visible:function(){return true==getUserOperation("u5") && "0"==getUserState();}
        }
        var item2 = {
            label:"启用",
            callback:startUser,
            icon:ICON + "start.gif",
            enable:function(){return true;},
            visible:function(){return true==getUserOperation("u4") && "1"==getUserState();}
        }
        var item3 = {
            label:"编辑",
            callback:editUserInfo,
            icon:ICON + "edit.gif",
            enable:function(){return true;},
            visible:function(){return true==getUserOperation("u2");}
        }
        var item4 = {
            label:"删除",
            callback:delUser,
            icon:ICON + "del.gif",
            enable:function(){return gridObj.canDelete();},
            visible:function(){return true==getUserOperation("u1");}
        }
        var item5 = {
            label:"移动到...",
            callback:moveUserTo,
            icon:ICON + "move.gif",
            enable:function(){return true;},
            visible:function(){return true==getUserOperation("u1");}
        }
        var item6 = {
            label:"下移",
            callback:moveUserDown,
            icon:ICON + "down.gif",
            enable:function(){return true;},
            visible:function(){return true==canMoveUserDown() && true==getUserOperation("u5");}
        }
        var item7 = {
            label:"上移",
            callback:moveUserUp,
            icon:ICON + "up.gif",
            enable:function(){return true;},
            visible:function(){return true==canMoveUserUp() && true==getUserOperation("u5");}
        }
        var item8 = {
            label:"隐藏列...",
            callback:function(){gridObj.hideCols();},
            icon:ICON + "hide_col.gif",
            enable:function(){return true;},
            visible:function(){return true;}
        }
        var item9 = {
            label:"查看",
            callback:function(){
                editUserInfo(false);
            },
            icon:ICON + "view.gif",
            enable:function(){return true;},
            visible:function(){return true==getUserOperation("u3");}
        }
        var item10 = {
            label:"授予角色",
            callback:setUserPermission,
            enable:function(){return true;},
            visible:function(){return "3"!=getUserGroupType();}
        }
        var item11 = {
            label:"搜索...",
            callback:function(){gridObj.search();},
            enable:function(){return true;},
            visible:function(){return true;}
        }
        var item12 = {
            label:"恢复默认顺序",
            callback:restoreDefaultSort,
            enable:function(){return true;},
            visible:function(){return true==canRestoreDefaultSort();}
        }
        var item13 = {
            label:"用户对应",
            callback:manualMappingUser,
            enable:function(){return true;},
            visible:function(){return true==isOtherUser() && true==getUserOperation("u6");}
        }
        var item14 = {
            label:"用户同步",
            callback:syncUser,
            enable:function(){return true;},
            visible:function(){return true==isOtherUser() && true==getUserOperation("u7");}
        }
 
        var item16 = {
            label:"用户导入到...",
            callback:importUserTo,
            enable:function(){return true;},
            visible:function(){return "3"==getUserGroupType();}
        }

        var menu1 = new Menu();
        menu1.addItem(item1);
        menu1.addItem(item2);
        menu1.addSeparator();
        menu1.addItem(item10);
        menu1.addItem(item15);
        menu1.addSeparator();
        menu1.addItem(item13);
        menu1.addItem(item14);
        menu1.addItem(item16);
        menu1.addSeparator();
        menu1.addItem(item9);
        menu1.addItem(item3);
        menu1.addItem(item4);
        menu1.addSeparator();
        menu1.addItem(item5);
        menu1.addItem(item6);
        menu1.addItem(item7);
        menu1.addSeparator();
        menu1.addItem(item12);
        menu1.addItem(item8);
        menu1.addItem(item11);
        //menu1.attachTo(gridObj,"contextmenu");
        gridObj.contextmenu = menu1;
    }
 
    /*
     *	grid初始化
     *	参数：	string:id                   grid数据相关树节点id
                string:applicationId        应用id
                string:groupType            组类型
     *	返回值：
     */
    function initGrid(id,applicationId,groupType){
        var gridObj = $("grid");
        Public.initHTC(gridObj,"isLoaded","onload",function(){
            loadGridEvents();
            loadGridData(id,applicationId,groupType,"1");//默认第1页
        });
    }
 
    function loadGridEvents(){
        var gridObj = $("grid");

        gridObj.onclickrow = function(){
            onClickRow(event);
        }
        gridObj.ondblclickrow = function(){
            onDblClickRow(event);
        }
        gridObj.onrightclickrow = function(){
            onRightClickRow(event);
        }
        gridObj.oninactiverow = function() {
            onInactiveRow(event);
        }
        gridObj.onsortrow = function() {
            onSortRow(event);
        }
    
    }
    /*
     *	grid加载数据
     *	参数：	string:treeID               grid数据相关树节点id
                string:applicationId        应用id
                string:groupType            组类型
                string:page                 页码
                string:sortName             排序字段
                string:direction            排序方向
     *	返回值：
     */
    function loadGridData(treeID,applicationId,groupType,page,sortName,direction){
        var cacheID = CACHE_TREE_NODE_GRID + treeID;
        var treeGrid = Cache.Variables.get(cacheID);
//        if(null==treeGrid){
            var p = new HttpRequestParams();
            p.url = URL_USER_LIST;
            p.setContent("groupId", treeID);
            p.setContent("applicationId", applicationId);
            p.setContent("groupType", groupType);
            p.setContent("page", page);
            if(null!=sortName && null!=direction){
                p.setContent("field", sortName);
                p.setContent("orderType", direction);
            }

            var request = new HttpRequest(p);
            request.onresult = function(){
                var sourceListNode = this.getNodeValue(XML_USER_LIST);
                var sourceListNodeID = cacheID+"."+XML_USER_LIST;

                var pageListNode = this.getNodeValue(XML_PAGE_LIST);
                var pageListNodeID = cacheID+"."+XML_PAGE_LIST;

                //给用户grid数据根节点增加groupType,applicationId等属性
                sourceListNode.setAttribute("groupType",groupType);
                sourceListNode.setAttribute("groupId",treeID);
                sourceListNode.setAttribute("applicationId",applicationId);

                //给当前排序列加上_direction属性
                if(null!=sortName && null!=direction){
                    var column = sourceListNode.selectSingleNode("//column[@name='" + sortName + "']");
                    if(null!=column){
                        column.setAttribute("_direction",direction);
                    }
                }

                Cache.XmlIslands.add(sourceListNodeID,sourceListNode);
                Cache.XmlIslands.add(pageListNodeID,pageListNode);
                Cache.Variables.add(cacheID,[sourceListNodeID,pageListNodeID]);

                loadGridDataFromCache(cacheID);
            }
            request.send();
//        }else{        
//            loadGridDataFromCache(cacheID);
//        }
    }
    /*
     *	grid从缓存加载数据
     *	参数：	string:cacheID   grid数据相关树节点id
     *	返回值：
     */
    function loadGridDataFromCache(cacheID){
        //重新创建grid工具条
        createGridToolBar(cacheID);

        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_USER_LIST);
        if(null!=xmlIsland){
            var gridObj = $("grid");
            gridObj.load(xmlIsland.node,null,"node");

            Focus.focus("gridTitle");
        }
    }
    /*
     *	创建grid工具条
     *	参数：	string:cacheID   grid数据相关树节点id
     *	返回值：
     */
    function createGridToolBar(cacheID){
        var toolbarObj = $("gridToolBar");

        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_PAGE_LIST);
        if(null==xmlIsland){
            toolbarObj.innerHTML = "";
        }else{
            initGridToolBar(toolbarObj,xmlIsland,function(page){
                var gridBtRefreshObj = $("gridBtRefresh");
                var gridObj = $("grid");

                if(true==gridObj.hasData_Xml()){
                    var tempXmlIsland = new XmlNode(gridObj.getXmlDocument());
                    var tempGroupId = tempXmlIsland.getAttribute("groupId");
                    var sortName = tempXmlIsland.getAttribute("sortName");
                    var direction = tempXmlIsland.getAttribute("direction");
                    if("search"!=tempGroupId){
                        //清除该组用户grid缓存
                        delCacheData(CACHE_TREE_NODE_GRID + tempGroupId);

                        var tempApplicationId = tempXmlIsland.getAttribute("applicationId");
                        var tempGroupType = tempXmlIsland.getAttribute("groupType");

                        loadGridData(tempGroupId,tempApplicationId,tempGroupType,page,sortName,direction);

                        //刷新工具条
                        onInactiveRow();
                    }else{
                        loadSearchGridData(cacheID,page,sortName,direction);
                    }
                }            
            });
        }
    }
 
    /*
     *	显示用户详细信息
     *	参数：	boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function editUserInfo(editable){
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var rowName = gridObj.getNamedNodeValue_Xml(rowIndex,"userName");
        var rowID = rowNode.getAttribute("id");
        var groupID = gridObj.getXmlDocument().getAttribute("groupId");
        if("search"==groupID){
            groupID = rowNode.getAttribute("groupId");
        }

        var groupType = gridObj.getXmlDocument().getAttribute("groupType");
        if("3"==groupType){//其他应用用户组的用户

            var phases = [];
            phases[0] = {page:"page1",label:"基本信息"};
            phases[1] = {page:"page8",label:"认证信息"};

            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadOtherUserDetailData(rowID,editable,groupID);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            if(false==editable){
                inf.label = OPERATION_VIEW.replace(/\$label/i,rowName);
                inf.SID = CACHE_VIEW_GRID_ROW_DETAIL + rowID;
            }else{
                inf.label = OPERATION_EDIT.replace(/\$label/i,rowName);
                inf.SID = CACHE_GRID_ROW_DETAIL + rowID;
            }
            inf.defaultPage = "page1";
            inf.phases = phases;
            inf.callback = callback;
            var tab = ws.open(inf);
        }else{//主、辅用户组的用户

            var phases = [];
            phases[0] = {page:"page1",label:"基本信息"};
            phases[1] = {page:"page8",label:"认证信息"};
            phases[2] = {page:"page2",label:"用户组"};
            phases[3] = {page:"page3",label:"角色"};

            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadUserDetailData(rowID,editable,groupID);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            if(false==editable){
                inf.label = OPERATION_VIEW.replace(/\$label/i,rowName);
                inf.SID = CACHE_VIEW_GRID_ROW_DETAIL + rowID;
            }else{
                inf.label = OPERATION_EDIT.replace(/\$label/i,rowName);
                inf.SID = CACHE_GRID_ROW_DETAIL + rowID;
            }
            inf.defaultPage = "page1";
            inf.phases = phases;
            inf.callback = callback;
            var tab = ws.open(inf);
        }
        
    }
    /*
     *	其他应用用户详细信息加载数据
     *	参数：	string:userID               用户id
                boolean:editable            是否可编辑(默认true)
                string:groupId              组id
                string:applicationId        应用id
                boolean:isNew               是否新增
     *	返回值：
     */
    function loadOtherUserDetailData(userID,editable,groupId,applicationId,isNew){
        if(false==editable){
            var cacheID = CACHE_VIEW_GRID_ROW_DETAIL + userID;
        }else{
            var cacheID = CACHE_GRID_ROW_DETAIL + userID;
        }
        var userDetail = Cache.Variables.get(cacheID);
        if(null==userDetail){
            var p = new HttpRequestParams();
            p.url = URL_USER_DETAIL;
            //如果是新增
            if(true==isNew){
                p.setContent("userId", "-10");
                p.setContent("groupId", groupId);
                p.setContent("applicationId", applicationId);
            }else{
                p.setContent("userId", userID);
                p.setContent("groupId", groupId);
            }

            var request = new HttpRequest(p);
            request.onresult = function(){
                var userInfoNode = this.getNodeValue(XML_USER_INFO);
                var authenticateInfoNode = this.getNodeValue(XML_AUTHENTICATE_INFO);
                var user2GroupTreeNode = Cache.XmlIslands.get(CACHE_MAIN_TREE).cloneNode(false);
                var user2GroupExistTreeNode = this.getNodeValue(XML_USER_TO_GROUP_EXIST_TREE);

                //只取同一应用系统
                var mainTreeNode = Cache.XmlIslands.get(CACHE_MAIN_TREE);
                var applicationNode = mainTreeNode.selectSingleNode(".//treeNode[@applicationId='"+applicationId+"']");
                if(null!=applicationNode){
                    applicationNode = applicationNode.cloneNode(true);
                    user2GroupTreeNode.appendChild(applicationNode);
                }

                var userInfoNodeID = cacheID+"."+XML_USER_INFO;
                var authenticateInfoNodeID = cacheID+"."+XML_AUTHENTICATE_INFO;
                var user2GroupTreeNodeID = cacheID+"."+XML_USER_TO_GROUP_TREE;
                var user2GroupExistTreeNodeID = cacheID+"."+XML_USER_TO_GROUP_EXIST_TREE;

                Cache.XmlIslands.add(userInfoNodeID,userInfoNode);
                Cache.XmlIslands.add(authenticateInfoNodeID,authenticateInfoNode);
                Cache.XmlIslands.add(user2GroupTreeNodeID,user2GroupTreeNode);
                Cache.XmlIslands.add(user2GroupExistTreeNodeID,user2GroupExistTreeNode);

                Cache.Variables.add(cacheID,[userInfoNodeID,authenticateInfoNodeID,user2GroupTreeNodeID,user2GroupExistTreeNodeID]);

                initOtherUserPages(cacheID,editable,isNew,groupId);
            }
            request.send();
        }else{
            initOtherUserPages(cacheID,editable,isNew,groupId);
        }
    }
    /*
     *	其他应用用户相关页加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
                boolean:isNew               是否新增
                string:groupId              组id
     *	返回值：
     */
    function initOtherUserPages(cacheID,editable,isNew,groupId){
        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadUserInfoFormData(cacheID,editable);
        });

        var page8FormObj = $("page8Form");
        Public.initHTC(page8FormObj,"isLoaded","oncomponentready",function(){
            loadAuthenticateInfoFormData(cacheID,editable);
        });

//        var page2TreeObj = $("page2Tree");
//        Public.initHTC(page2TreeObj,"isLoaded","oncomponentready",function(){
//            loadUser2GroupTreeData(cacheID);
//        });
//
//        var page2Tree2Obj = $("page2Tree2");
//        Public.initHTC(page2Tree2Obj,"isLoaded","oncomponentready",function(){
//            loadUser2GroupExistTreeData(cacheID);
//
//            //标记当前page2Tree2是其他应用的
//            page2Tree2Obj.groupType = "3";
//        });
//
//        //设置添加按钮操作
//        var page2BtAddObj = $("page2BtAdd");
//        page2BtAddObj.onclick = function(){
//            addTreeNode(page2TreeObj,page2Tree2Obj,function(treeNode){
//                var result = {
//                    "error":false,
//                    "message":"",
//                    "stop":true
//                };
//                var rootNode = page2Tree2Obj.getTreeNodeById("_rootId");
//                if(null!=rootNode.node.selectSingleNode("treeNode")){
//                    result.error = true;
//                    result.message = "其他应用一个用户对应用户组只允许一个";
//                    result.stop = true;
//                }
//                return result;
//            });
//        }
//
//        //设置删除按钮操作
//        var page2BtDelObj = $("page2BtDel");
//        page2BtDelObj.onclick = function(){
//            removeTreeNode(page2Tree2Obj);
//        }
//
//        //设置搜索
//        var page2BtSearchObj = $("page2BtSearch");
//        var page2KeywordObj = $("page2Keyword");
//        attachSearchTree(page2TreeObj,page2BtSearchObj,page2KeywordObj);

        //设置翻页按钮显示状态
        var page1BtPrevObj = $("page1BtPrev");
        var page8BtPrevObj = $("page8BtPrev");
//        var page2BtPrevObj = $("page2BtPrev");
        var page1BtNextObj = $("page1BtNext");
        var page8BtNextObj = $("page8BtNext");
//        var page2BtNextObj = $("page2BtNext");
        page1BtPrevObj.style.display = "none";
        page8BtPrevObj.style.display = "";
//        page2BtPrevObj.style.display = "";
        page1BtNextObj.style.display = "";
        page8BtNextObj.style.display = "none";
//        page2BtNextObj.style.display = "none";

        //设置保存按钮操作
        var page1BtSaveObj = $("page1BtSave");
        var page8BtSaveObj = $("page8BtSave");
//        var page2BtSaveObj = $("page2BtSave");
        page1BtSaveObj.disabled = editable==false?true:false;
        page8BtSaveObj.disabled = editable==false?true:false;
        page1BtSaveObj.onclick = page8BtSaveObj.onclick = function(){
            saveOtherUser(cacheID,isNew,groupId);
        }
    }
    /*
     *	用户详细信息加载数据
     *	参数：	string:userID               用户id
                boolean:editable            是否可编辑(默认true)
                string:groupId              用户组id
                string:applicationId        应用id
                boolean:isNew               是否新增
                string:disabled             组状态(1停用/0启用)
     *	返回值：
     */
    function loadUserDetailData(userID,editable,groupId,applicationId,isNew,disabled){
        if(false==editable){
            var cacheID = CACHE_VIEW_GRID_ROW_DETAIL + userID;
        }else{
            var cacheID = CACHE_GRID_ROW_DETAIL + userID;
        }
        var userDetail = Cache.Variables.get(cacheID);
        if(null==userDetail){
            var p = new HttpRequestParams();
            p.url = URL_USER_DETAIL;
            //如果是新增
            if(true==isNew){
                p.setContent("userId", "-10");
                p.setContent("groupId", groupId);
                p.setContent("applicationId", applicationId);
                p.setContent("disabled", disabled);
            }else{
                p.setContent("userId", userID);
                p.setContent("groupId", groupId);
            }

            var request = new HttpRequest(p);
            request.onresult = function(){
                var userInfoNode = this.getNodeValue(XML_USER_INFO);
                var authenticateInfoNode = this.getNodeValue(XML_AUTHENTICATE_INFO);
                var user2GroupExistTreeNode = this.getNodeValue(XML_USER_TO_GROUP_EXIST_TREE);
                var user2GroupTreeNode = Cache.XmlIslands.get(CACHE_MAIN_TREE).cloneNode(false);
                var user2RoleTreeNode = this.getNodeValue(XML_USER_TO_ROLE_TREE);
                var user2RoleGridNode = this.getNodeValue(XML_USER_TO_ROLE_EXIST_TREE);

                //只保留主、辅助用户组
                var mainTreeNode = Cache.XmlIslands.get(CACHE_MAIN_TREE);
                var GroupType1Node = mainTreeNode.selectSingleNode("./treeNode[@groupType='1']");
                if(null!=GroupType1Node){
                    GroupType1Node = GroupType1Node.cloneNode(true);
                    GroupType1Node.setAttribute("canselected","0");
                    user2GroupTreeNode.appendChild(GroupType1Node);
                }
                var GroupType2Node = mainTreeNode.selectSingleNode("./treeNode[@groupType='2']");
                if(null!=GroupType2Node){
                    GroupType2Node = GroupType2Node.cloneNode(true);
                    GroupType2Node.setAttribute("canselected","0");
                    user2GroupTreeNode.appendChild(GroupType2Node);
                }

                var userInfoNodeID = cacheID+"."+XML_USER_INFO;
                var authenticateInfoNodeID = cacheID+"."+XML_AUTHENTICATE_INFO;
                var user2GroupExistTreeNodeID = cacheID+"."+XML_USER_TO_GROUP_EXIST_TREE;
                var user2GroupTreeNodeID = cacheID+"."+XML_USER_TO_GROUP_TREE;
                var user2RoleTreeNodeID = cacheID+"."+XML_USER_TO_ROLE_TREE;
                var user2RoleGridNodeID = cacheID+"."+XML_USER_TO_ROLE_EXIST_TREE;

                Cache.XmlIslands.add(userInfoNodeID,userInfoNode);
                Cache.XmlIslands.add(authenticateInfoNodeID,authenticateInfoNode);
                Cache.XmlIslands.add(user2GroupExistTreeNodeID,user2GroupExistTreeNode);
                Cache.XmlIslands.add(user2GroupTreeNodeID,user2GroupTreeNode);
                Cache.XmlIslands.add(user2RoleTreeNodeID,user2RoleTreeNode);
                Cache.XmlIslands.add(user2RoleGridNodeID,user2RoleGridNode);

                Cache.Variables.add(cacheID,[userInfoNodeID,authenticateInfoNodeID,user2GroupExistTreeNodeID,user2GroupTreeNodeID,user2RoleTreeNodeID,user2RoleGridNodeID]);

                initUserPages(cacheID,editable,isNew,groupId);
            }
            request.send();
        }else{
            initUserPages(cacheID,editable,isNew,groupId);
        }
    }
    /*
     *	用户相关页加载数据
     *	参数：	string:cacheID          缓存数据id
                boolean:isNew           是否新增
                string:groupId          用户组id
     *	返回值：
     */
    function initUserPages(cacheID,editable,isNew,groupId){
        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadUserInfoFormData(cacheID,editable);
        });

        var page8FormObj = $("page8Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadAuthenticateInfoFormData(cacheID,editable);
        });

        var page2TreeObj = $("page2Tree");
        Public.initHTC(page2TreeObj,"isLoaded","oncomponentready",function(){
            loadUser2GroupTreeData(cacheID);
        });

        var page2Tree2Obj = $("page2Tree2");
        Public.initHTC(page2Tree2Obj,"isLoaded","oncomponentready",function(){
            loadUser2GroupExistTreeData(cacheID);

            //标记当前page2Tree2是主(辅助)用户组
            page2Tree2Obj.groupType = "1";
        });

        var page3TreeObj = $("page3Tree");
        Public.initHTC(page3TreeObj,"isLoaded","oncomponentready",function(){
            loadUser2RoleTreeData(cacheID);
        });

        var page3Tree2Obj = $("page3Tree2");
        Public.initHTC(page3Tree2Obj,"isLoaded","oncomponentready",function(){
            loadUser2RoleExistTreeData(cacheID);
        });

        //设置翻页按钮显示状态
        var page1BtPrevObj = $("page1BtPrev");
        var page8BtPrevObj = $("page8BtPrev");
        var page2BtPrevObj = $("page2BtPrev");
        var page3BtPrevObj = $("page3BtPrev");
        var page1BtNextObj = $("page1BtNext");
        var page8BtNextObj = $("page8BtNext");
        var page2BtNextObj = $("page2BtNext");
        var page3BtNextObj = $("page3BtNext");
        page1BtPrevObj.style.display = "none";
        page8BtPrevObj.style.display = "";
        page2BtPrevObj.style.display = "";
        page3BtPrevObj.style.display = "";
        page1BtNextObj.style.display = "";
        page8BtNextObj.style.display = "";
        page2BtNextObj.style.display = "";
        page3BtNextObj.style.display = "none";

        //设置保存按钮操作
        var page1BtSaveObj = $("page1BtSave");
        var page8BtSaveObj = $("page8BtSave");
        var page2BtSaveObj = $("page2BtSave");
        var page3BtSaveObj = $("page3BtSave");
        page1BtSaveObj.disabled = editable==false?true:false;
        page8BtSaveObj.disabled = editable==false?true:false;
        page2BtSaveObj.disabled = editable==false?true:false;
        page3BtSaveObj.disabled = editable==false?true:false;
        page1BtSaveObj.onclick = page8BtSaveObj.onclick = page2BtSaveObj.onclick = page3BtSaveObj.onclick = function(){
            saveUser(cacheID,isNew,groupId);
        }

        //设置搜索
        var page2BtSearchObj = $("page2BtSearch");
        var page2KeywordObj = $("page2Keyword");
        attachSearchTree(page2TreeObj,page2BtSearchObj,page2KeywordObj);

        //设置搜索
        var page3BtSearchObj = $("page3BtSearch");
        var page3KeywordObj = $("page3Keyword");
        attachSearchTree(page3TreeObj,page3BtSearchObj,page3KeywordObj);

        //设置添加按钮操作
        var page2BtAddObj = $("page2BtAdd");
        page2BtAddObj.disabled = editable==false?true:false;
        page2BtAddObj.onclick = function(){
            addTreeNode(page2TreeObj,page2Tree2Obj,function(treeNode){
                var result = {
                    "error":false,
                    "message":"",
                    "stop":true
                };
                var groupType = treeNode.getAttribute("groupType");
                if("1"==groupType && true==hasSameAttributeTreeNode(page2Tree2Obj,"groupType",groupType)){
                    result.error = true;
                    result.message = "一个用户对应主用户组只允许一个";
                    result.stop = true;
                }
                return result;
            });
        }

        //设置添加按钮操作
        var page3BtAddObj = $("page3BtAdd");
        page3BtAddObj.disabled = editable==false?true:false;
        page3BtAddObj.onclick = function(){
            addTreeNode(page3TreeObj,page3Tree2Obj,function(treeNode){
                var result = {
                    "error":false,
                    "message":"",
                    "stop":true
                };
                if("1"==treeNode.getAttribute("isGroup")){
                    result.error = true;
                    result.message = null;
                    result.stop = false;
                }
                return result;
            });
        }

        //设置删除按钮操作
        var page2BtDelObj = $("page2BtDel");
        page2BtDelObj.disabled = editable==false?true:false;
        page2BtDelObj.onclick = function(){
            removeTreeNode(page2Tree2Obj);
        }

        //设置删除按钮操作
        var page3BtDelObj = $("page3BtDel");
        page3BtDelObj.disabled = editable==false?true:false;
        page3BtDelObj.onclick = function(){
             removeTreeNode(page3Tree2Obj);
        }
    }
    /*
     *	用户信息xform加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function loadUserInfoFormData(cacheID,editable){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_USER_INFO);
        if(null!=xmlIsland){
            var page1FormObj = $("page1Form");
            page1FormObj.editable = editable==false?"false":"true";
            page1FormObj.load(xmlIsland.node,null,"node");

            page1FormObj.ondatachange = function(){
                //2007-3-1 离开提醒
                attachReminder(cacheID);

                var name = event.result.name;
                var newValue = event.result.newValue;
                if("password" == name){
                    var password = newValue;
                    var id = page1FormObj.getData("id");
                    var loginName = page1FormObj.getData("loginName");
                    checkPasswordAvailable(page1FormObj,URL_CHECK_PASSWORD,password,id,loginName);
                }
            }
        }
    }
    /*
     *	认证信息xform加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function loadAuthenticateInfoFormData(cacheID,editable){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_AUTHENTICATE_INFO);
        if(null!=xmlIsland){
            var page8FormObj = $("page8Form");
            page8FormObj.editable = editable==false?"false":"true";
            page8FormObj.load(xmlIsland.node,null,"node");

            //2007-3-1 离开提醒
            attachReminder(cacheID,page8FormObj);
        }
    }
    /*
     *	用户对用户组tree加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadUser2GroupTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_USER_TO_GROUP_TREE);
        if(null!=xmlIsland){
            var page2TreeObj = $("page2Tree");
            page2TreeObj.load(xmlIsland.node);
            page2TreeObj.research = true;
        }
    }
    /*
     *	用户对用户组tree加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadUser2GroupExistTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_USER_TO_GROUP_EXIST_TREE);
        if(null!=xmlIsland){
            var page2Tree2Obj = $("page2Tree2");
            page2Tree2Obj.load(xmlIsland.node);
        }
    }
    /*
     *	用户对角色tree加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadUser2RoleTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_USER_TO_ROLE_TREE);
        if(null!=xmlIsland){
            var page3TreeObj = $("page3Tree");
            page3TreeObj.load(xmlIsland.node);
            page3TreeObj.research = true;
        }
    }
    /*
     *	用户对角色tree加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadUser2RoleExistTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_USER_TO_ROLE_EXIST_TREE);
        if(null!=xmlIsland){
            var page3Tree2Obj = $("page3Tree2");
            page3Tree2Obj.load(xmlIsland.node);
        }
    }
    /*
     *	资源树初始化
     *	参数：	string:cacheID      缓存数据ID
     *	返回值：
     */
    function initTree(cacheID){
        var treeObj = $("tree");
        Public.initHTC(treeObj,"isLoaded","oncomponentready",function(){
            initTreeData(cacheID);
        });
    }
    /*
     *	资源树加载数据
     *	参数：
     *	返回值：
     */
    function initTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID);
        if(null!=xmlIsland){
            var treeObj = $("tree");
            treeObj.load(xmlIsland.node);

            treeObj.onTreeNodeActived = function(eventObj){
                onTreeNodeActived(eventObj);
            }
            treeObj.onTreeNodeDoubleClick = function(eventObj){
                onTreeNodeDoubleClick(eventObj);
            }
            treeObj.onTreeNodeMoved = function(eventObj){
                onTreeNodeMoved(eventObj);
            }
            treeObj.onTreeNodeRightClick = function(eventObj){
                onTreeNodeRightClick(eventObj);
            }
        }    
    }
    /*
     *	其他应用用户组详细信息加载数据
     *	参数：	string:treeID               树节点id
                boolean:editable            是否可编辑(默认true)
                string:parentID             父节点id，新增时需要
                boolean:isNew               是否新增
                string:disabled           组状态(1停用/0启用)
     *	返回值：
     */
    function loadOtherGroupDetailData(treeID,editable,parentID,groupType,applicationId,isNew,disabled){
        if(false==editable){
            var cacheID = CACHE_VIEW_TREE_NODE_DETAIL + treeID;
        }else{
            var cacheID = CACHE_TREE_NODE_DETAIL + treeID;
        }
        var treeDetail = Cache.Variables.get(cacheID);
        if(null==treeDetail){
            var p = new HttpRequestParams();
            p.url = URL_GROUP_DETAIL;
            if(true==isNew){
                if(null==groupType){
					p.setContent("toGroupId","-4");
                    p.setContent("groupType", "3");
				}else{
					p.setContent("toGroupId",parentID);
				    p.setContent("groupType", groupType);
				}
                p.setContent("groupId", "-10");
                p.setContent("applicationId", applicationId);
                p.setContent("disabled", disabled);
            }else{
                p.setContent("groupId", treeID);
                p.setContent("groupType", groupType);
            }

            var request = new HttpRequest(p);
            request.onresult = function(){
                var groupInfoNode = this.getNodeValue(XML_GROUP_INFO);
                var group2UserTreeNode = Cache.XmlIslands.get(CACHE_MAIN_TREE).cloneNode(false);
                var group2UserGridNode = this.getNodeValue(XML_GROUP_TO_USER_EXIST_TREE);

                //只保留同一应用系统组
                if("-4"==parentID){//其他应用组节点上新建用户组时先不出现树
                
                }else{
                    var mainTreeNode = Cache.XmlIslands.get(CACHE_MAIN_TREE);
                    var applicationNode = mainTreeNode.selectSingleNode(".//treeNode[@applicationId='"+applicationId+"']").cloneNode(true);
                    group2UserTreeNode.appendChild(applicationNode);
                }

                var groupInfoNodeID = cacheID+"."+XML_GROUP_INFO;
                var group2UserTreeNodeID = cacheID+"."+XML_GROUP_TO_USER_TREE;
                var group2UserGridNodeID = cacheID+"."+XML_GROUP_TO_USER_EXIST_TREE;

                Cache.XmlIslands.add(groupInfoNodeID,groupInfoNode);
                Cache.XmlIslands.add(group2UserTreeNodeID,group2UserTreeNode);
                Cache.XmlIslands.add(group2UserGridNodeID,group2UserGridNode);

                Cache.Variables.add(cacheID,[groupInfoNodeID,group2UserTreeNodeID,group2UserGridNodeID]);

                initOtherGroupPages(cacheID,editable,isNew,parentID);
            }
            request.send();
        }else{
            initOtherGroupPages(cacheID,editable,isNew,parentID);
        }
    }
    /*
     *	用户组相关页加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
                boolean:isNew               是否新增
                string:parentID             父节点id，新增时需要
     *	返回值：
     */
    function initOtherGroupPages(cacheID,editable,isNew,parentID){
        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadGroupInfoFormData(cacheID,editable);
        });

        var page4TreeObj = $("page4Tree");
        Public.initHTC(page4TreeObj,"isLoaded","oncomponentready",function(){
            loadGroup2UserTreeData(cacheID);
        });

        var page4Tree2Obj = $("page4Tree2");
        Public.initHTC(page4Tree2Obj,"isLoaded","oncomponentready",function(){
            clearTreeData(page4Tree2Obj);
        });

        var page4Tree3Obj = $("page4Tree3");
        Public.initHTC(page4Tree3Obj,"isLoaded","oncomponentready",function(){
            loadGroup2UserExistTreeData(cacheID);
        });

        //设置翻页按钮显示状态
        var page1BtPrevObj = $("page1BtPrev");
        var page4BtPrevObj = $("page4BtPrev");
        var page1BtNextObj = $("page1BtNext");
        var page4BtNextObj = $("page4BtNext");
        page1BtPrevObj.style.display = "none";
        page4BtPrevObj.style.display = "none";
        page1BtNextObj.style.display = "none";
        page4BtNextObj.style.display = "none";

        //设置搜索
        var page4BtSearchObj = $("page4BtSearch");
        var page4KeywordObj = $("page4Keyword");
        attachSearchTree(page4TreeObj,page4BtSearchObj,page4KeywordObj);

        //设置搜索
        var page4BtSearch2Obj = $("page4BtSearch2");
        var page4Keyword2Obj = $("page4Keyword2");
        attachSearchTree(page4Tree2Obj,page4BtSearch2Obj,page4Keyword2Obj);

        //设置保存按钮操作
        var page1BtSaveObj = $("page1BtSave");
        page1BtSaveObj.disabled = editable==false?true:false;
        page1BtSaveObj.onclick = function(){
            saveOtherGroup(cacheID,isNew,parentID);
        }

        //设置添加按钮操作
        var page4BtAddObj = $("page4BtAdd");
        page4BtAddObj.disabled = editable==false?true:false;
        page4BtAddObj.onclick = function(){
            addTreeNode(page4Tree2Obj,page4Tree3Obj);
        }

        //设置删除按钮操作
        var page4BtDelObj = $("page4BtDel");
        page4BtDelObj.disabled = editable==false?true:false;
        page4BtDelObj.onclick = function(){
             removeTreeNode(page4Tree3Obj);
        }
    }
    /*
     *	树节点数据详细信息加载数据
     *	参数：	string:treeID               树节点id
                boolean:editable            是否可编辑(默认true)
                string:parentID             父节点id
                boolean:isNew               是否新增
                string:disabled           组状态(1停用/0启用)
     *	返回值：
     */
    function loadGroupDetailData(treeID,editable,parentID,groupType,applicationId,isNew,disabled){
        if(false==editable){
            var cacheID = CACHE_VIEW_TREE_NODE_DETAIL + treeID;
        }else{
            var cacheID = CACHE_TREE_NODE_DETAIL + treeID;
        }
        var treeDetail = Cache.Variables.get(cacheID);
        if(null==treeDetail){
            var p = new HttpRequestParams();
            p.url = URL_GROUP_DETAIL;
            p.setContent("groupType", groupType);
            p.setContent("applicationId", applicationId);
            if(true==isNew){
                p.setContent("toGroupId", parentID);                
                p.setContent("groupId", -10);              
                p.setContent("disabled", disabled);
            }else{
                p.setContent("groupId", treeID);
            }

            var request = new HttpRequest(p);
            request.onresult = function(){
                var groupInfoNode = this.getNodeValue(XML_GROUP_INFO);
                var group2UserTreeNode = this.getNodeValue(XML_GROUP_TO_USER_TREE);
                var group2UserTreeNode = Cache.XmlIslands.get(CACHE_MAIN_TREE).cloneNode(false);
                var group2UserGridNode = this.getNodeValue(XML_GROUP_TO_USER_EXIST_TREE);
                var group2RoleTreeNode = this.getNodeValue(XML_GROUP_TO_ROLE_TREE);
                var group2RoleGridNode = this.getNodeValue(XML_GROUP_TO_ROLE_EXIST_TREE);

                //只保留主、辅助用户组
                var mainTreeNode = Cache.XmlIslands.get(CACHE_MAIN_TREE);
                var GroupType1Node = mainTreeNode.selectSingleNode("./treeNode[@groupType='1']");
                if(null!=GroupType1Node){
                    GroupType1Node = GroupType1Node.cloneNode(true);
                    group2UserTreeNode.appendChild(GroupType1Node);
                }
                var GroupType2Node = mainTreeNode.selectSingleNode("./treeNode[@groupType='2']");
                if(null!=GroupType2Node){
                    GroupType2Node = GroupType2Node.cloneNode(true);
                    group2UserTreeNode.appendChild(GroupType2Node);
                }

                var groupInfoNodeID = cacheID+"."+XML_GROUP_INFO;
                var group2UserTreeNodeID = cacheID+"."+XML_GROUP_TO_USER_TREE;
                var group2UserGridNodeID = cacheID+"."+XML_GROUP_TO_USER_EXIST_TREE;
                var group2RoleTreeNodeID = cacheID+"."+XML_GROUP_TO_ROLE_TREE;
                var group2RoleGridNodeID = cacheID+"."+XML_GROUP_TO_ROLE_EXIST_TREE;

                Cache.XmlIslands.add(groupInfoNodeID,groupInfoNode);
                Cache.XmlIslands.add(group2UserTreeNodeID,group2UserTreeNode);
                Cache.XmlIslands.add(group2UserGridNodeID,group2UserGridNode);
                Cache.XmlIslands.add(group2RoleTreeNodeID,group2RoleTreeNode);
                Cache.XmlIslands.add(group2RoleGridNodeID,group2RoleGridNode);

                Cache.Variables.add(cacheID,[groupInfoNodeID,group2UserTreeNodeID,group2UserGridNodeID,group2RoleTreeNodeID,group2RoleGridNodeID]);

                initGroupPages(cacheID,editable,isNew,parentID,groupType);
            }
            request.send();
        }else{
            initGroupPages(cacheID,editable,isNew,parentID,groupType);
        }
    }
    /*
     *	用户组相关页加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
                boolean:isNew               是否新增
                string:parentID             父节点id
                string:groupType            组类型
     *	返回值：
     */
    function initGroupPages(cacheID,editable,isNew,parentID,groupType){
        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadGroupInfoFormData(cacheID,editable,groupType);
        });

        if("2"==groupType){
            var page4TreeObj = $("page4Tree");
            Public.initHTC(page4TreeObj,"isLoaded","oncomponentready",function(){
                loadGroup2UserTreeData(cacheID);
            });

            var page4Tree2Obj = $("page4Tree2");
            Public.initHTC(page4Tree2Obj,"isLoaded","oncomponentready",function(){
                clearTreeData(page4Tree2Obj);
            });

            var page4Tree3Obj = $("page4Tree3");
            Public.initHTC(page4Tree3Obj,"isLoaded","oncomponentready",function(){
                loadGroup2UserExistTreeData(cacheID);
            });
        }

        var page3TreeObj = $("page3Tree");
        Public.initHTC(page3TreeObj,"isLoaded","oncomponentready",function(){
            loadGroup2RoleTreeData(cacheID);
        });

        var page3Tree2Obj = $("page3Tree2");
        Public.initHTC(page3Tree2Obj,"isLoaded","oncomponentready",function(){
            loadGroup2RoleExistTreeData(cacheID);
        });

        //设置翻页按钮显示状态
        var page1BtPrevObj = $("page1BtPrev");
        var page4BtPrevObj = $("page4BtPrev");
        var page3BtPrevObj = $("page3BtPrev");
        var page1BtNextObj = $("page1BtNext");
        var page4BtNextObj = $("page4BtNext");
        var page3BtNextObj = $("page3BtNext");
        page1BtPrevObj.style.display = "none";
        page4BtPrevObj.style.display = "";
        page3BtPrevObj.style.display = "";
        page1BtNextObj.style.display = "";
        page4BtNextObj.style.display = "";
        page3BtNextObj.style.display = "none";

        //设置保存按钮操作
        var page1BtSaveObj = $("page1BtSave");
        var page4BtSaveObj = $("page4BtSave");
        var page3BtSaveObj = $("page3BtSave");
        page1BtSaveObj.disabled = editable==false?true:false;
        page4BtSaveObj.disabled = editable==false?true:false;
        page3BtSaveObj.disabled = editable==false?true:false;
        page1BtSaveObj.onclick = page4BtSaveObj.onclick = page3BtSaveObj.onclick = function(){
            saveGroup(cacheID,isNew,parentID,groupType);
        }

        if("2"==groupType){
            //设置搜索
            var page4BtSearchObj = $("page4BtSearch");
            var page4KeywordObj = $("page4Keyword");
            attachSearchTree(page4TreeObj,page4BtSearchObj,page4KeywordObj);

            //设置搜索
            var page4BtSearch2Obj = $("page4BtSearch2");
            var page4Keyword2Obj = $("page4Keyword2");
            attachSearchTree(page4Tree2Obj,page4BtSearch2Obj,page4Keyword2Obj);
        }

        //设置搜索
        var page3BtSearchObj = $("page3BtSearch");
        var page3KeywordObj = $("page3Keyword");
        attachSearchTree(page3TreeObj,page3BtSearchObj,page3KeywordObj);

        //设置添加按钮操作
        var page3BtAddObj = $("page3BtAdd");
        page3BtAddObj.disabled = editable==false?true:false;
        page3BtAddObj.onclick = function(){
            addTreeNode(page3TreeObj,page3Tree2Obj,function(treeNode){
                var result = {
                    "error":false,
                    "message":"",
                    "stop":true
                };
                if("1"==treeNode.getAttribute("isGroup")){
                    result.error = true;
                    result.message = null;
                    result.stop = false;
                }
                return result;
            });
        }

        //设置添加按钮操作
        var page4BtAddObj = $("page4BtAdd");
        page4BtAddObj.disabled = editable==false?true:false;
        page4BtAddObj.onclick = function(){
            addTreeNode(page4Tree2Obj,page4Tree3Obj);
        }

        //设置删除按钮操作
        var page3BtDelObj = $("page3BtDel");
        page3BtDelObj.disabled = editable==false?true:false;
        page3BtDelObj.onclick = function(){
             removeTreeNode(page3Tree2Obj);
        }

        //设置删除按钮操作
        var page4BtDelObj = $("page4BtDel");
        page4BtDelObj.disabled = editable==false?true:false;
        page4BtDelObj.onclick = function(){
             removeTreeNode(page4Tree3Obj);
        }
    }
    /*
     *	用户组信息xform加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
                string:groupType            组类型
     *	返回值：
     */
    function loadGroupInfoFormData(cacheID,editable,groupType){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_GROUP_INFO);
        if(null!=xmlIsland){
            var page1FormObj = $("page1Form");
            page1FormObj.editable = editable==false?"false":"true";
            page1FormObj.load(xmlIsland.node,null,"node");

            page1FormObj.ondatachange = function(){
                //2007-3-1 离开提醒
                attachReminder(cacheID);
            }
        }
    }
    /*
     *	用户组对用户tree加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadGroup2UserTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_GROUP_TO_USER_TREE);
        if(null!=xmlIsland){
            var page4TreeObj = $("page4Tree");
            page4TreeObj.load(xmlIsland.node);
            page4TreeObj.research = true;

            page4TreeObj.onTreeNodeDoubleClick = function(eventObj){
                onPage4TreeNodeDoubleClick(eventObj);
            }
        }
    }
    /*
     *	用户组对用户tree加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadGroup2UserExistTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_GROUP_TO_USER_EXIST_TREE);
        if(null!=xmlIsland){
            var page4Tree3Obj = $("page4Tree3");
            page4Tree3Obj.load(xmlIsland.node);
        }
    }
    /*
     *	用户组对角色tree加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadGroup2RoleTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_GROUP_TO_ROLE_TREE);
        if(null!=xmlIsland){
            var page3TreeObj = $("page3Tree");
            page3TreeObj.load(xmlIsland.node);
            page3TreeObj.research = true;
        }
    }
    /*
     *	用户组对角色tree加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadGroup2RoleExistTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_GROUP_TO_ROLE_EXIST_TREE);
        if(null!=xmlIsland){
            var page3Tree2Obj = $("page3Tree2");
            page3Tree2Obj.load(xmlIsland.node);
        }
    }
    /*
     *	聚焦初始化
     *	参数：	
     *	返回值：
     */
    function initFocus(){
        var treeTitleObj = $("treeTitle");
        var statusTitleObj = $("statusTitle");
        var gridTitleObj = $("gridTitle");

        Focus.register(treeTitleObj.firstChild);
        Focus.register(statusTitleObj.firstChild);
        Focus.register(gridTitleObj);
    }
    /*
     *	事件绑定初始化
     *	参数：	
     *	返回值：
     */
    function initEvents(){
        var treeBtRefreshObj = $("treeBtRefresh");
        var treeTitleBtObj = $("treeTitleBt");
        var statusTitleBtObj = $("statusTitleBt");
        var paletteBtObj = $("paletteBt");

        var treeTitleObj = $("treeTitle");
        var statusTitleObj = $("statusTitle");
        var gridTitleObj = $("gridTitle");
        
        Event.attachEvent(treeBtRefreshObj,"click",onClickTreeBtRefresh);
        Event.attachEvent(treeTitleBtObj,"click",onClickTreeTitleBt);
        Event.attachEvent(statusTitleBtObj,"click",onClickStatusTitleBt);
        Event.attachEvent(paletteBtObj,"click",onClickPaletteBt);

        Event.attachEvent(treeTitleObj,"click",onClickTreeTitle);
        Event.attachEvent(statusTitleObj,"click",onClickStatusTitle);
        Event.attachEvent(gridTitleObj,"click",onClickGridTitle);
    }
    /*
     *	点击树节点
     *	参数：	Object:eventObj     模拟事件对象
     *	返回值：
     */
    function onTreeNodeActived(eventObj){
        var treeTitleObj = $("treeTitle");
        Focus.focus(treeTitleObj.firstChild.id);

        showTreeNodeStatus({id:"ID",name:"名称",creatorName:"创建者",createTime:"创建时间",updatorName:"修改者",updateTime:"修改时间"});
 
    }
    /*
     *	双击树节点
     *	参数：	Object:eventObj     模拟事件对象
     *	返回值：
     */
    function onTreeNodeDoubleClick(eventObj){
        var treeNode = eventObj.treeNode;
        var appType = getAppType();
        var isRoot = isRootNode();
        getTreeOperation(treeNode,function(_operation){
            if(null==appType && true!=isRoot){
                var canShowUserList = checkOperation("6",_operation);
                var canView = checkOperation("5",_operation);
                var canEdit = checkOperation("4",_operation);

                if(true==canShowUserList){
                    showUserList();
                }else{
                    if(true==canEdit){
                        editTreeNode();                    
                    }else if(true==canView){
                        editTreeNode(false);
                    }                
                }
            }
        });
    }
 
    function onTreeNodeRightClick(eventObj){
        var treeObj = $("tree");
        var treeNode = eventObj.treeNode;

        showTreeNodeStatus({id:"ID",name:"名称",creatorName:"创建者",createTime:"创建时间",updatorName:"修改者",updateTime:"修改时间"});

        var x = eventObj.clientX;
        var y = eventObj.clientY;
        getTreeOperation(treeNode,function(_operation){
            if(null!=treeObj.contextmenu){
                treeObj.contextmenu.show(x,y);                
            }
        });
    }
 
    function onTreeNodeMoved(eventObj){
        sortGroupTo(eventObj);
    }
 
    function onPage4TreeNodeDoubleClick(eventObj){
        var treeObj = $("page4Tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var applicationId = treeNode.getAttribute("applicationId");
            initPage4Tree2(id,applicationId);
        }
    }
 
    function onClickRow(eventObj){    
        Focus.focus("gridTitle");
 
    }
 
    function onDblClickRow(eventObj){
        var rowIndex = eventObj.result.rowIndex_Xml;
        getGridOperation(rowIndex,function(_operation){
            //检测编辑权限
            var edit = checkOperation("u2",_operation);
            var view = checkOperation("u3",_operation);

            if(true==edit){
                editUserInfo(true);
            }else if(true==view){
                editUserInfo(false);
            }
        });
    }
 
    function onRightClickRow(eventObj){
        var gridObj = $("grid");
        var rowIndex = eventObj.result.rowIndex_Xml;
        var x = event.clientX;
        var y = event.clientY;

        getGridOperation(rowIndex,function(_operation){
            gridObj.contextmenu.show(x,y);
            loadToolBar(_operation);
        });
    }
 
 
    /*
     *	保存用户组
     *	参数：	string:cacheID      缓存数据ID
                boolean:isNew       是否新增
                string:parentID     父节点id
                string:groupType    组类型
     *	返回值：
     */
    function saveGroup(cacheID,isNew,parentID,groupType){
        //校验page1Form数据有效性
        var page1FormObj = $("page1Form");
        if(false==page1FormObj.checkForm()){
            switchToPhase(ws,"page1");
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SAVE_GROUP;

        //是否提交
        var flag = false;
        
        var groupCache = Cache.Variables.get(cacheID);
        if(null!=groupCache){       

            //用户组基本信息
            var groupInfoNode = Cache.XmlIslands.get(cacheID+"."+XML_GROUP_INFO);
            if(null!=groupInfoNode){
                var groupInfoDataNode = groupInfoNode.selectSingleNode(".//data");
                if(null!=groupInfoDataNode){
                    flag = true;

                    var prefix = groupInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                    p.setXFormContent(groupInfoDataNode,prefix);
                }
            }

            //用户组对用户
            if("2"==groupType){
                var group2UserNode = Cache.XmlIslands.get(cacheID+"."+XML_GROUP_TO_USER_EXIST_TREE);
                if(null!=group2UserNode){
                    var group2UserDataIDs = getTreeNodeIds(group2UserNode,"./treeNode//treeNode");
                    if(0<group2UserDataIDs.length){
                        flag = true;
                        p.setContent(XML_GROUP_TO_USER_EXIST_TREE,group2UserDataIDs.join(","));
                    }
                }
            }

            //用户组对角色
            var group2RoleNode = Cache.XmlIslands.get(cacheID+"."+XML_GROUP_TO_ROLE_EXIST_TREE);
            if(null!=group2RoleNode){
                var group2RoleDataIDs = getTreeNodeIds(group2RoleNode,"./treeNode//treeNode");
                if(0<group2RoleDataIDs.length){
                    flag = true;
                    p.setContent(XML_GROUP_TO_ROLE_EXIST_TREE,group2RoleDataIDs.join(","));
                }
            }
        }

        if(true==flag){
            var request = new HttpRequest(p);
            //同步按钮状态
            var page1BtSaveObj = $("page1BtSave");
            var page4BtSaveObj = $("page4BtSave");
            var page3BtSaveObj = $("page3BtSave");
            syncButton([page1BtSaveObj,page4BtSaveObj,page3BtSaveObj],request);

            request.onresult = function(){
                if(true==isNew){
                    //解除提醒
                    detachReminder(cacheID);

                    var treeNode = this.getNodeValue(XML_MAIN_TREE).selectSingleNode("treeNode");
                    appendTreeNode(parentID,treeNode);

                    var ws = $("ws");
                    ws.closeActiveTab();
                }
            }
            request.onsuccess = function(){
                if(true!=isNew){
                    //解除提醒
                    detachReminder(cacheID);

                    var id = cacheID.trim(CACHE_TREE_NODE_DETAIL);

                    //辅助用户组，更新批次信息
                    if("2"==groupType){
                        var treeObj = $("tree");
                        var disabled = treeObj.getTreeNodeById(id).getAttribute("disabled");
                        var img = "user_group";
                        if("1"==disabled){
                            img += "_2";
                        }
                        img += ".gif";
                        modifyTreeNode(id,"icon",ICON + img, false);
                    }

                    //更新树节点名称
                    var name = page1FormObj.getData("name");
                    modifyTreeNode(id,"name",name,true);
                }
            }
            request.send();
        }
    }
    /*
     *	保存用户
     *	参数：	string:cacheID          缓存数据ID
                boolean:isNew           是否新增
                string:groupId          用户组id
     *	返回值：
     */
    function saveUser(cacheID,isNew,groupId){
        //校验page1Form,page8Form数据有效性
        var page1FormObj = $("page1Form");
        var page8FormObj = $("page8Form");
        if(false==page1FormObj.checkForm()){
            switchToPhase(ws,"page1");
            return;
        }
        if(false==page8FormObj.checkForm()){
            switchToPhase(ws,"page8");
            return;
        }
        if("0" == page1FormObj.securityLevel){
            switchToPhase(ws,"page1");
            showPasswordSecurityLevel(page1FormObj);
            return;
        }

        //校验用户对组page2Tree2数据有效性
        var page2Tree2Obj = $("page2Tree2");
        var user2GroupNode = new XmlNode(page2Tree2Obj.getTreeNodeById("_rootId").node);
        var groupType1TreeNode = user2GroupNode.selectSingleNode(".//treeNode[@groupType='1']");
        if(null==groupType1TreeNode){
            switchToPhase(ws,"page2");
            var balloon = Balloons.create("至少要有一个主用户组");
            balloon.dockTo(page2Tree2Obj);
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SAVE_USER;

        //是否提交
        var flag = false;
        
        var userCache = Cache.Variables.get(cacheID);
        if(null!=userCache){

            //用户基本信息
            var userInfoNode = Cache.XmlIslands.get(cacheID+"."+XML_USER_INFO);
            if(null!=userInfoNode){
                var userInfoDataNode = userInfoNode.selectSingleNode(".//data");
                if(null!=userInfoDataNode){
                    flag = true;

                    var prefix = userInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                    p.setXFormContent(userInfoDataNode,prefix);
                }
            }

            //认证基本信息
            var authenticateInfoNode = Cache.XmlIslands.get(cacheID+"."+XML_AUTHENTICATE_INFO);
            if(null!=authenticateInfoNode){
                var authenticateInfoDataNode = authenticateInfoNode.selectSingleNode(".//data");
                if(null!=userInfoDataNode){
                    flag = true;

                    var prefix = authenticateInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                    p.setXFormContent(authenticateInfoDataNode,prefix);
                }
            }

            //用户对用户组
            var user2GroupNode = Cache.XmlIslands.get(cacheID+"."+XML_USER_TO_GROUP_EXIST_TREE);
            if(null!=user2GroupNode){
                var user2GroupDataIDs = getTreeNodeIds(user2GroupNode,"./treeNode//treeNode");
                if(0<user2GroupDataIDs.length){
                    flag = true;
                    p.setContent(XML_USER_TO_GROUP_EXIST_TREE,user2GroupDataIDs.join(","));

					//主用户组id
					var mainGroupId = groupType1TreeNode.getAttribute("id");
                    p.setContent("mainGroupId",mainGroupId);
                }
            }

            //用户对角色
            var user2RoleNode = Cache.XmlIslands.get(cacheID+"."+XML_USER_TO_ROLE_EXIST_TREE);
            if(null!=user2RoleNode){
                var user2RoleDataIDs = getTreeNodeIds(user2RoleNode,"./treeNode//treeNode");
                if(0<user2RoleDataIDs.length){
                    flag = true;
                    p.setContent(XML_USER_TO_ROLE_EXIST_TREE,user2RoleDataIDs.join(","));
                }
            }
        }

        if(true==flag){
            var request = new HttpRequest(p);
            //同步按钮状态
            var page1BtSaveObj = $("page1BtSave");
            var page2BtSaveObj = $("page2BtSave");
            var page3BtSaveObj = $("page3BtSave");
            var page8BtSaveObj = $("page8BtSave");
            syncButton([page1BtSaveObj,page2BtSaveObj,page3BtSaveObj,page8BtSaveObj],request);

            request.onsuccess = function(){
                //解除提醒
                detachReminder(cacheID);

                //清除该组用户grid缓存
                delCacheData(CACHE_TREE_NODE_GRID + groupId);

                //如果当前grid显示为此用户所在组，则刷新grid
                var gridObj = $("grid");
                if(true==gridObj.hasData_Xml()){
                    var tempXmlIsland = new XmlNode(gridObj.getXmlDocument());
                    var tempGroupId = tempXmlIsland.getAttribute("groupId");
                    if(tempGroupId==groupId){
                        var tempApplicationId = tempXmlIsland.getAttribute("applicationId");
                        var tempGroupType = tempXmlIsland.getAttribute("groupType");

                        loadGridData(tempGroupId,tempApplicationId,tempGroupType,"1");//默认第1页

                        //刷新工具条
                        onInactiveRow();
                    }
                }

                if(true==isNew){
                    var ws = $("ws");
                    ws.closeActiveTab();
                }
            }
            request.send();;
        }
    }

    /*
     *	显示用户列表
 
     */
    function showUserList(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var applicationId = treeNode.getAttribute("applicationId");
            var groupType = treeNode.getAttribute("groupType");
            initGrid(id,applicationId,groupType);
        }
    }
    /*
     *	新建用户 
     */
    function addNewUser(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();
            var treeType = treeNode.getAttribute("groupType");
            var applicationId = treeNode.getAttribute("applicationId");
            var disabled = treeNode.getAttribute("disabled");

            var userName = "用户";
            var userID = new Date().valueOf();

            if("1"==treeType || "2"==treeType){//主用户组和辅助用户组

                var phases = [];
                phases[0] = {page:"page1",label:"基本信息"};
                phases[1] = {page:"page8",label:"认证信息"};
                phases[2] = {page:"page2",label:"用户组"};
                phases[3] = {page:"page3",label:"角色"};

                var callback = {};
                callback.onTabClose = function(eventObj){
                    delCacheData(eventObj.tab.SID);
                };
                callback.onTabChange = function(){
                    setTimeout(function(){
                        loadUserDetailData(userID,true,treeID,applicationId,true,disabled);
                    },TIMEOUT_TAB_CHANGE);
                };

                var inf = {};
                inf.defaultPage = "page1";
                inf.label = OPERATION_ADD.replace(/\$label/i,userName);
                inf.phases = phases;
                inf.callback = callback;
                inf.SID = CACHE_GRID_ROW_DETAIL + userID;
                var tab = ws.open(inf);

            }else{//其他应用

                var phases = [];
                phases[0] = {page:"page1",label:"基本信息"};
                phases[1] = {page:"page8",label:"认证信息"};
//                phases[2] = {page:"page2",label:"用户组"};

                var callback = {};
                callback.onTabClose = function(eventObj){
                    delCacheData(eventObj.tab.SID);
                };
                callback.onTabChange = function(){
                    setTimeout(function(){
                        loadOtherUserDetailData(userID,true,treeID,applicationId,true);
                    },TIMEOUT_TAB_CHANGE);
                };

                var inf = {};
                inf.defaultPage = "page1";
                inf.label = OPERATION_ADD.replace(/\$label/i,userName);
                inf.phases = phases;
                inf.callback = callback;
                inf.SID = CACHE_GRID_ROW_DETAIL + userID;
                var tab = ws.open(inf);

            }
        }
    }
    /*
     *	新建用户组
     *	参数：	
     *	返回值：
     */
    function addNewGroup(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();
            var treeType = treeNode.getAttribute("groupType");
            var applicationId = treeNode.getAttribute("applicationId");
            var disabled = treeNode.getAttribute("disabled");

            var groupName = "用户组";
            var groupID = new Date().valueOf();

            switch(treeType){
                case "1"://主用户组
                    var phases = [];
                    phases[0] = {page:"page1",label:"基本信息"};
                    phases[1] = {page:"page3",label:"角色"};

                    var callback = {};
                    callback.onTabClose = function(eventObj){
                        delCacheData(eventObj.tab.SID);
                    };
                    callback.onTabChange = function(){
                        setTimeout(function(){
                            loadGroupDetailData(groupID,true,treeID,treeType,applicationId,true,disabled);
                        },TIMEOUT_TAB_CHANGE);
                    };

                    var inf = {};
                    inf.defaultPage = "page1";
                    inf.label = OPERATION_ADD.replace(/\$label/i,groupName);
                    inf.phases = phases;
                    inf.callback = callback;
                    inf.SID = CACHE_TREE_NODE_DETAIL + groupID;
                    var tab = ws.open(inf);
                    break;
                case "2"://辅助用户组
                    var phases = [];
                    phases[0] = {page:"page1",label:"基本信息"};
                    phases[1] = {page:"page4",label:"用户"};
                    phases[2] = {page:"page3",label:"角色"};

                    var callback = {};
                    callback.onTabClose = function(eventObj){
                        delCacheData(eventObj.tab.SID);
                    };
                    callback.onTabChange = function(){
                        setTimeout(function(){
                            loadGroupDetailData(groupID,true,treeID,treeType,applicationId,true,disabled);
                        },TIMEOUT_TAB_CHANGE);
                    };

                    var inf = {};
                    inf.defaultPage = "page1";
                    inf.label = OPERATION_ADD.replace(/\$label/i,groupName);
                    inf.phases = phases;
                    inf.callback = callback;
                    inf.SID = CACHE_TREE_NODE_DETAIL + groupID;
                    var tab = ws.open(inf);
                    break;
                case "3"://其他应用下的用户组
                    var phases = [];
                    phases[0] = {page:"page1",label:"基本信息"};
                    phases[1] = {page:"page4",label:"用户"};
                    phases = null;//暂时不启用

                    var callback = {};
                    callback.onTabClose = function(eventObj){
                        delCacheData(eventObj.tab.SID);
                    };
                    callback.onTabChange = function(){
                        setTimeout(function(){
                            loadOtherGroupDetailData(groupID,true,treeID,treeType,applicationId,true,disabled);
                        },TIMEOUT_TAB_CHANGE);
                    };

                    var inf = {};
                    inf.defaultPage = "page1";
                    inf.label = OPERATION_ADD.replace(/\$label/i,groupName);
                    inf.phases = phases;
                    inf.callback = callback;
                    inf.SID = CACHE_TREE_NODE_DETAIL + groupID;
                    var tab = ws.open(inf);
                    break;
                default://应用节点
                    var phases = [];
                    phases[0] = {page:"page1",label:"基本信息"};
                    phases[1] = {page:"page4",label:"用户"};
                    phases = null;//暂时不启用

                    var callback = {};
                    callback.onTabClose = function(eventObj){
                        delCacheData(eventObj.tab.SID);
                    };
                    callback.onTabChange = function(){
                        setTimeout(function(){
                            loadOtherGroupDetailData(groupID,true,treeID,treeType,applicationId,true);
                        },TIMEOUT_TAB_CHANGE);
                    };

                    var inf = {};
                    inf.defaultPage = "page1";
                    inf.label = OPERATION_ADD.replace(/\$label/i,groupName);
                    inf.phases = phases;
                    inf.callback = callback;
                    inf.SID = CACHE_TREE_NODE_DETAIL + groupID;
                    var tab = ws.open(inf);
                    break;
            }
        }
    }
    /*
     *	编辑树节点
     */
    function editTreeNode(editable){
        var resourceTypeId = getResourceTypeId();
        switch(resourceTypeId){
            case "5":
                editApplication(editable);
                break;
            default:
                editGroupInfo(editable);
                break;
        }
    }
    /*
     *	编辑组信息
     */
    function editGroupInfo(editable){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();
            var treeType = treeNode.getAttribute("groupType");
            var applicationId = treeNode.getAttribute("applicationId");

            switch(treeType){
                case "1"://主用户组
                    var phases = [];
                    phases[0] = {page:"page1",label:"基本信息"};
                    phases[1] = {page:"page3",label:"角色"};

                    var callback = {};
                    callback.onTabClose = function(eventObj){
                        delCacheData(eventObj.tab.SID);
                    };
                    callback.onTabChange = function(){
                        setTimeout(function(){
                            loadGroupDetailData(treeID,editable,null,treeType,applicationId);
                        },TIMEOUT_TAB_CHANGE);
                    };

                    var inf = {};
                    if(false==editable){
                        inf.label = OPERATION_VIEW.replace(/\$label/i,treeName);
                        inf.SID = CACHE_VIEW_TREE_NODE_DETAIL + treeID;
                    }else{
                        inf.label = OPERATION_EDIT.replace(/\$label/i,treeName);
                        inf.SID = CACHE_TREE_NODE_DETAIL + treeID;
                    }
                    inf.defaultPage = "page1";
                    inf.phases = phases;
                    inf.callback = callback;
                    var tab = ws.open(inf);
                    break;
                case "2"://辅助用户组
                    var phases = [];
                    phases[0] = {page:"page1",label:"基本信息"};
                    phases[1] = {page:"page4",label:"用户"};
                    phases[2] = {page:"page3",label:"角色"};

                    var callback = {};
                    callback.onTabClose = function(eventObj){
                        delCacheData(eventObj.tab.SID);
                    };
                    callback.onTabChange = function(){
                        setTimeout(function(){
                            loadGroupDetailData(treeID,editable,null,treeType,applicationId);
                        },TIMEOUT_TAB_CHANGE);
                    };

                    var inf = {};
                    if(false==editable){
                        inf.label = OPERATION_VIEW.replace(/\$label/i,treeName);
                        inf.SID = CACHE_VIEW_TREE_NODE_DETAIL + treeID;
                    }else{
                        inf.label = OPERATION_EDIT.replace(/\$label/i,treeName);
                        inf.SID = CACHE_TREE_NODE_DETAIL + treeID;
                    }
                    inf.defaultPage = "page1";
                    inf.phases = phases;
                    inf.callback = callback;
                    var tab = ws.open(inf);
                    break;
                case "3"://其他应用下的用户组
                default://应用
                    var phases = [];
                    phases[0] = {page:"page1",label:"基本信息"};
                    phases[1] = {page:"page4",label:"用户"};
                    phases = null;//暂时不启用
                    
                    var callback = {};
                    callback.onTabClose = function(eventObj){
                        delCacheData(eventObj.tab.SID);
                    };
                    callback.onTabChange = function(){
                        setTimeout(function(){
                            loadOtherGroupDetailData(treeID,editable,treeID,treeType,applicationId);
                        },TIMEOUT_TAB_CHANGE);
                    };

                    var inf = {};
                    if(false==editable){
                        inf.label = OPERATION_VIEW.replace(/\$label/i,treeName);
                        inf.SID = CACHE_VIEW_TREE_NODE_DETAIL + treeID;
                    }else{
                        inf.label = OPERATION_EDIT.replace(/\$label/i,treeName);
                        inf.SID = CACHE_TREE_NODE_DETAIL + treeID;
                    }
                    inf.defaultPage = "page1";
                    inf.phases = phases;
                    inf.callback = callback;
                    var tab = ws.open(inf);
                    break;
            }
        }
    }
 
    function delTreeNode(){
        if(true!=confirm("您确定要删除吗？")){
            return;
        }
        var resourceTypeId = getResourceTypeId();
        switch(resourceTypeId){
            case "5":
                delApplication();
                break;
            default:
                delGroup();
                break;
        }
    }
 
    function delGroup(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            var treeType = treeNode.getAttribute("groupType");
            var applicationId = treeNode.getAttribute("applicationId");
            
            var p = new HttpRequestParams();
            p.url = URL_DEL_GROUP;
            p.setContent("groupId",treeID);
            p.setContent("groupType",treeType);
            p.setContent("applicationId", applicationId);

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                var parentNode = treeNode.getParent();
                if(null!=parentNode){
                    treeObj.setActiveTreeNode(parentNode.getId());
                }
                //从树上删除
                treeObj.removeTreeNode(treeNode);
            }
            request.send();

        }        
    }
 
    function moveUserTo(){
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var userID = rowNode.getAttribute("id");
        var userName = rowNode.getAttribute("userName");
        var groupType = gridObj.getXmlDocument().getAttribute("groupType");
        var applicationID = gridObj.getXmlDocument().getAttribute("applicationId");
        var groupID = gridObj.getXmlDocument().getAttribute("groupId");
        if("search"==groupID){
			if(null!=rowIndex){
				var curRowNode = gridObj.getRowNode_Xml(rowIndex);
				groupID = curRowNode.getAttribute("groupId");
			}
        }

        //弹出窗口数据从后台获取
        var xmlIsland = null;

        var group = getGroup(groupID,xmlIsland,"移动\""+userName+"\"到",groupType,applicationID,"2");
        if(null!=group){
            var p = new HttpRequestParams();
            p.url = URL_MOVE_USER;
            p.setContent("toGroupId",group.id);
            p.setContent("userId",userID);
            p.setContent("groupId",groupID);

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                //从grid上删除
                gridObj.delRow_Xml(rowIndex);

                //刷新工具条
                onInactiveRow();
            }
            request.send();
        }
    }

    /*
     *	同一父节点下移动用户组
     */
    function sortGroupTo(eventObj){
        var treeObj = $("tree");
        var movedTreeNode = eventObj.movedTreeNode;
        var toTreeNode = eventObj.toTreeNode;
        var moveState = eventObj.moveState;

        var moveId = movedTreeNode.getId();
        var appType = movedTreeNode.getAttribute("appType");
        var toId = toTreeNode.getId();

        if(null!=movedTreeNode){
            if("-2"==moveId || "-3"==moveId || "-4"==moveId){
                alert("不能移动此节点!");
                return;
            }
        }

        var p = new HttpRequestParams();
        if(null==appType){//用户组
            p.url = URL_SORT_GROUP;
            p.setContent("toGroupId",toId);
            p.setContent("groupId",moveId);
            p.setContent("direction",moveState);//-1目标上方,1目标下方
        }else{//应用
            p.url = URL_SORT_APPLICATION;
            p.setContent("toAppId",toId);
            p.setContent("appId",moveId);
            p.setContent("direction",moveState);//-1目标上方,1目标下方
        }

        var request = new HttpRequest(p);
        request.onsuccess = function(){
            //alert("移动用户组成功");

            //移动树节点
            treeObj.moveTreeNode(movedTreeNode, toTreeNode, moveState);
        }
        request.send();
    }
    /*
     *	获取节点ID
     */
    function getTreeId(){
        return getTreeAttribute("id");   
    }
    /*
     *	获取用户组状态
     */
    function getGroupState(){
        return getTreeAttribute("disabled");
    }
    /*
     *	获取用户组类型
     */
    function getGroupType(){
        return getTreeAttribute("groupType");
    }
    /*
     *	获取应用类型
     */
    function getAppType(){
        return getTreeAttribute("appType");
    }
    /*
     *	获取资源类型
     */
    function getResourceTypeId(){
        return getTreeAttribute("resourceTypeId");
    }
    /*
     *	停用用户组
     */
    function stopGroup(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            var treeType = treeNode.getAttribute("groupType");
            var applicationId = treeNode.getAttribute("applicationId");

            var p = new HttpRequestParams();
            p.url = URL_STOP_GROUP;
            p.setContent("groupId",treeID);
            p.setContent("groupType",treeType);
            p.setContent("applicationId",applicationId);
            p.setContent("disabled","1");

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                //设置停用状态
                var xmlNode = new XmlNode(treeNode.node);
                refreshGroupStates(xmlNode,"1");

                //刷新工具条
                loadToolBarData(treeNode);
            }
            request.send();
        }
    }
    /*
     *	启用用户组
     *	参数：	
     *	返回值：
     */
    function startGroup(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            var treeType = treeNode.getAttribute("groupType");
            var applicationId = treeNode.getAttribute("applicationId");

            var p = new HttpRequestParams();
            p.url = URL_START_GROUP;
            p.setContent("groupId",treeID);
            p.setContent("groupType",treeType);
            p.setContent("applicationId",applicationId);
            p.setContent("disabled","0");

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                //设置启用状态
                var xmlNode = new XmlNode(treeNode.node);
                refreshGroupStates(xmlNode,"0");

                //刷新工具条
                loadToolBarData(treeNode);
            }
            request.send();
        }
    }
    /*
     *	刷新父子相关用户组停用启用状态
     *	参数：	XmlNode:curNode         XmlNode实例
                string:state            停/启用状态
     *	返回值：
     */
    function refreshGroupStates(curNode,state){
        var curNodeID = curNode.getAttribute("id");
        refreshGroupState(curNode,state);

        //顶层节点启/停用全部下溯，其余节点启用上溯，停用下溯
        if("1"==state){    //true==isTopLevelNode(curNode)  UMS没顶层结点 2007-4-3 modified by wuzy
            var childNodes = curNode.selectNodes(".//treeNode");
            for(var i=0,iLen=childNodes.length;i<iLen;i++){                
                refreshGroupState(childNodes[i],state);
            }
        }else if("0"==state){
            while(null!=curNode && false==isRootNode(curNodeID) && null==curNode.getAttribute("appType")){
                refreshGroupState(curNode,state);

                curNode = curNode.getParent();
                if(null!=curNode){
                    curNodeID = curNode.getAttribute("id");
                }
            }
        }

        var treeObj = $("tree");
        treeObj.reload();    
    }
    /*
     *	是否根节点
     *	参数：	    string:id           树节点id(如未提供则自动取树当前节点)
     *	返回值：    boolean:flag        是否根节点
     */
    function isRootNode(id){
        if(null==id){
            var treeObj = $("tree");
            var treeNode = treeObj.getActiveTreeNode();
            if(null!=treeNode){
                id = treeNode.getId();
            }            
        }
        var flag = ("-1"==id || "-2"==id || "-3"==id || "-4"==id || "-7"==id || "-8"==id || "-9"==id);   // 已注册用户组 和 未注册用户组
        return flag;
    }
    /*
     *	是否是其他组
     *	参数：	    string:id           树节点id(如未提供则自动取树当前节点)
     *	返回值：    boolean:flag        是否是其他组
     */
    function isOtherGroup(id){
        if(null==id){
            var treeObj = $("tree");
            var treeNode = treeObj.getActiveTreeNode();
            if(null!=treeNode){
                id = treeNode.getId();
            }            
        }
        var flag = ("-4"==id);
        return flag;
    }
    /*
     *	是否自注册用户组节点
     *	参数：	    string:id           树节点id(如未提供则自动取树当前节点)
     *	返回值：    boolean:flag        是否根节点
     */
    function isSelfRegisterNode(id){
        if(null==id){
            var treeObj = $("tree");
            var treeNode = treeObj.getActiveTreeNode();
            if(null!=treeNode){
                id = treeNode.getId();
            }            
        }
        var flag = ("-7"==id || "-8"==id || "-9"==id);
        return flag;
    }
    /*
     *	是否顶层节点(注：非根节点)
     *	参数：	XmlNode:xmlNode       XmlNode实例
     *	返回值：
     */
    function isTopLevelNode(xmlNode){
        var parentNode = xmlNode.getParent();
        var parentNodeID = parentNode.getAttribute("id");

        return isRootNode(parentNodeID);
    }
    /*
     *	刷新用户组停用启用状态
     *	参数：	XmlNode:xmlNode         XmlNode实例
                string:state            停/启用状态
     *	返回值：
     */
    function refreshGroupState(xmlNode,state){
        xmlNode.setAttribute("disabled",state);
        xmlNode.setAttribute("icon",ICON + "user_group" + (state=="0"?"":"_2") + ".gif");

        //清除用户列表缓存
        var treeID = xmlNode.getAttribute("id");
        delCacheData(CACHE_TREE_NODE_GRID + treeID);
    }
    /*
     *	获取用户状态
     */
    function getUserState(){
        var userState = null;
        var gridObj = $("grid");
        var curRowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        if(null!=curRowIndex){
            var curRowNode = gridObj.getRowNode_Xml(curRowIndex);
            userState = curRowNode.getAttribute("disabled");
        }
        return userState;   
    }
    /*
     *	停用用户
     */
    function stopUser() {
        var gridObj = $("grid");
        var curRowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        if(null!=curRowIndex){
            var curRowNode = gridObj.getRowNode_Xml(curRowIndex);
            var curRowID = curRowNode.getAttribute("id");

            var p = new HttpRequestParams();
            p.url = URL_STOP_USER;
            p.setContent("userId",curRowID);
            p.setContent("accountState","1");

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                //成功后设置状态
                gridObj.modifyNamedNode_Xml(curRowIndex,"disabled","1");
                gridObj.modifyNamedNode_Xml(curRowIndex,"icon",ICON + "user_2.gif");
 
            }
            request.send();
        }
    }
    /*
     *	启用用户
     */
    function startUser(){

        var gridObj = $("grid");
        var curRowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        if(null!=curRowIndex){
            var curRowNode = gridObj.getRowNode_Xml(curRowIndex);
            var curRowID = curRowNode.getAttribute("id");
            var groupID = gridObj.getXmlDocument().getAttribute("groupId");
            if("search"==groupID){
                groupID = curRowNode.getAttribute("groupId");
            }

            var p = new HttpRequestParams();
            p.url = URL_START_USER;
            p.setContent("userId",curRowID);
            p.setContent("groupId",groupID);
            p.setContent("accountState","0");

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                //成功后设置状态
                gridObj.modifyNamedNode_Xml(curRowIndex,"disabled","0");
                gridObj.modifyNamedNode_Xml(curRowIndex,"icon",ICON + "user.gif");

                //启用组
                var treeObj = $("tree");
                var treeNode = treeObj.getTreeNodeById(groupID);
                if(null!=treeNode){
                    var xmlNode = new XmlNode(treeNode.node);
                    refreshGroupStates(xmlNode,"0");
                }
 
            }
            request.send();
        }
    }
    /*
     *	同步用户组
     *	参数：	string:mode     同步方式(1完全同步/2单向同步)
     *	返回值：
     */
    function syncGroup(mode){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeNodeID = treeNode.getId();

            var p = new HttpRequestParams();
            p.url = URL_SYNC_GROUP;

            var applicationId = treeNode.getAttribute("applicationId");
            var dbGroupId = treeNode.getAttribute("dbGroupId");
            p.setContent("applicationId",applicationId);
            p.setContent("groupId",treeNodeID);
            p.setContent("dbGroupId",dbGroupId);
            p.setContent("mode",mode);

            var request = new HttpRequest(p);
 
            request.onresult = function(){
                var data = this.getNodeValue("ProgressInfo");
                var progress = new Progress(URL_SYNC_PROGRESS,data,URL_CANCEL_SYNC_PROGRESS);
                progress.oncomplete = function(){
                    loadInitData();
                }
                progress.start();
            }
            request.send();
        }    
    }

    /*
     *	page4Tree2初始化
     *	参数：	string:id					相关树节点id
                string:applicationId        应用id
                string:groupType            组类型
     *	返回值：
     */
    function initPage4Tree2(id,applicationId,groupType){
        var page4Tree2Obj = $("page4Tree2");
        Public.initHTC(page4Tree2Obj,"isLoaded","oncomponentready",function(){
            loadPage4Tree2Data(id,applicationId);
        });
    }
    /*
     *	grid加载数据
     *	参数：	string:treeID				相关树节点id
                string:applicationId        应用id
                string:groupType            组类型
     *	返回值：
     */
    function loadPage4Tree2Data(treeID,applicationId){
        var cacheID = CACHE_GROUP_TO_USER_GRID + treeID;
        var treeGrid = Cache.Variables.get(cacheID);

		var p = new HttpRequestParams();
		p.url = URL_GROUP_TO_USER_LIST;
		p.setContent("groupId", treeID);
		p.setContent("applicationId", applicationId)

		var request = new HttpRequest(p);
		request.onresult = function(){
			var sourceListNode = this.getNodeValue(XML_GROUP_TO_USER_LIST_TREE);
			var sourceListNodeID = cacheID+"."+XML_GROUP_TO_USER_LIST_TREE;

			Cache.XmlIslands.add(sourceListNodeID,sourceListNode);
			Cache.Variables.add(cacheID,[sourceListNodeID]);

			loadPage4Tree2DataFromCache(cacheID);
		}
		request.send();
    }
    /*
     *	tree从缓存加载数据
     *	参数：	string:cacheID   grid数据相关树节点id
     *	返回值：
     */
    function loadPage4Tree2DataFromCache(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_GROUP_TO_USER_LIST_TREE);
        if(null!=xmlIsland){
            var page4Tree2Obj = $("page4Tree2");
            page4Tree2Obj.load(xmlIsland.node);
            page4Tree2Obj.research = true;
        }
    }
    /*
     *	删除用户
     *	参数：	
     *	返回值：
     */
    function delUser(){
        if(true!=confirm("您确定要删除吗？")){
            return;
        }
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var rowID = rowNode.getAttribute("id");
        var groupID = gridObj.getXmlDocument().getAttribute("groupId");
        if("search"==groupID){
            groupID = rowNode.getAttribute("groupId");
        }
        var groupType = gridObj.getXmlDocument().getAttribute("groupType");

        var p = new HttpRequestParams();
        p.url = URL_DEL_USER;
        p.setContent("userId",rowID);
        p.setContent("groupId",groupID);
        p.setContent("groupType",groupType);

        var request = new HttpRequest(p);
        request.onsuccess = function(){
            //从grid上删除
            gridObj.delRow_Xml(rowIndex,true,true);

            //刷新工具条
            onInactiveRow();
        }
        request.send();
    }
 
  
    /*
     *	搜索用户
     */
    function searchUser(){

        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();
            var groupType = treeNode.getAttribute("groupType");
            var applicationID = treeNode.getAttribute("applicationId");
            var cacheID = CACHE_SEARCH_USER + treeID;

            var condition = window.showModalDialog("searchuser.htm",{groupId:treeID,groupType:groupType,applicationId:applicationID,title:"搜索\""+treeName+"\"下的用户"},"dialogWidth:250px;dialogHeight:250px;");
            if(null!=condition){
                Cache.Variables.add("condition",condition);
                loadSearchGridData(cacheID,1);
            }
        }
    }
    /*
     *	根据条件获取搜索结果
     *	参数：	string:cacheID      缓存数据id
                string:page         页码
                string:sortName     排序字段
                string:direction    排序方向
     *	返回值：
     */
    function loadSearchGridData(cacheID,page,sortName,direction){
        var condition = Cache.Variables.get("condition");
        if(null!=condition){
            var p = new HttpRequestParams();
            p.url = URL_SEARCH_USER;

            var xmlReader = new XmlReader(condition.dataXml);
            var dataNode = new XmlNode(xmlReader.documentElement);
            p.setXFormContent(dataNode,condition.prefix);
            p.setContent("page",page);
            if(null!=sortName && null!=direction){
                p.setContent("field", sortName);
                p.setContent("orderType", direction);
            }

            var row = dataNode.selectSingleNode("row");
            var groupType = row.getCDATA("groupType");
            var applicationID = row.getCDATA("applicationId");

			p.setContent("applicationId", applicationID);

            var request = new HttpRequest(p);
            request.onresult = function(){
                var sourceListNode = this.getNodeValue(XML_USER_LIST);
                var sourceListNodeID = cacheID+"."+XML_USER_LIST;

                var pageListNode = this.getNodeValue(XML_PAGE_LIST);
                var pageListNodeID = cacheID+"."+XML_PAGE_LIST;

                //给用户grid数据根节点增加groupType,applicationId等属性
                sourceListNode.setAttribute("groupType",groupType);
                sourceListNode.setAttribute("groupId","search");//搜索用户情况比较特殊，所以置特殊值用以区分
                sourceListNode.setAttribute("applicationId",applicationID);

                //给当前排序列加上_direction属性
                if(null!=sortName && null!=direction){
                    var column = sourceListNode.selectSingleNode("//column[@name='" + sortName + "']");
                    if(null!=column){
                        column.setAttribute("_direction",direction);
                    }
                }

                Cache.XmlIslands.add(sourceListNodeID,sourceListNode);
                Cache.XmlIslands.add(pageListNodeID,pageListNode);
                Cache.Variables.add(cacheID,[sourceListNodeID,pageListNodeID]);

                
                initSearchGrid(cacheID);
            }
            request.send();;
        }
    }
    /*
     *	初始化搜索用户grid
     *	参数：	string:cacheID      缓存数据id
     *	返回值：
     */
    function initSearchGrid(cacheID){
        var gridObj = $("grid");
        Public.initHTC(gridObj,"isLoaded","onload",function(){
            loadGridDataFromCache(cacheID);
            loadGridEvents();

            //刷新工具条
            onInactiveRow();
        });    
    }
    /*
     *	更新page4Tree的数据
     *	参数：	string:cacheID            缓存数据id
                string:applicationId      应用id
     *	返回值：
     */
    function updatePage4TreeData(cacheID,applicationId){
        var group2UserTreeNode = Cache.XmlIslands.get(CACHE_MAIN_TREE).cloneNode(false);
        var group2UserTreeNodeID = cacheID+"."+XML_GROUP_TO_USER_TREE;
        Cache.XmlIslands.add(group2UserTreeNodeID,group2UserTreeNode);

        //只保留同一应用系统组
        var mainTreeNode = Cache.XmlIslands.get(CACHE_MAIN_TREE);
        var applicationNode = mainTreeNode.selectSingleNode(".//treeNode[@applicationId='"+applicationId+"']");
        if(null!=applicationNode){
            applicationNode = applicationNode.cloneNode(true);
            group2UserTreeNode.appendChild(applicationNode);
        }

        //重新载入树
        var page4TreeObj = $("page4Tree");
        Public.initHTC(page4TreeObj,"isLoaded","oncomponentready",function(){
            loadGroup2UserTreeData(cacheID);
        });

        //切换应用后，清除page4Tree2数据
        var page4Tree2Obj = $("page4Tree2");
        Public.initHTC(page4Tree2Obj,"isLoaded","oncomponentready",function(){
            clearTreeData(page4Tree2Obj);
        });

        //切换应用后，清除page4Tree3数据
        var page4Tree3Obj = $("page4Tree3");
        Public.initHTC(page4Tree3Obj,"isLoaded","oncomponentready",function(){
            clearTreeData(page4Tree3Obj);
        });
    }
 
    /*
     *	弹出模态窗口选择用户组
     *	参数：	string:id               组/用户id
                XmlNode:xmlIsland       如果有该值，树数据不从后台取
                string:title            弹出窗口标题
                string:groupType        用户组类型
                string:applicationId    用户组所在应用
                string:type             1:添加组/2:添加用户/3:查看组
     *	返回值：
     */
    function getGroup(id,xmlIsland,title,groupType,applicationId,type){
        var group = window.showModalDialog("grouptree.htm",{id:id,xmlIsland:xmlIsland,title:title,groupType:groupType,applicationId:applicationId,type:type},"dialogWidth:300px;dialogHeight:400px;");
        return group;
    }
    /*
     *	初始化密码
     */
    function resetPassword(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();
            var pwd = "";
            var first = true;
            while(""==pwd){
                if(true!=first){
                    alert("密码不能为空");
                }
                pwd = prompt("请输入新密码","","初始化\""+treeName+"\"的密码",true);
                first = false;
            }
            if(null!=pwd){
                var p = new HttpRequestParams();
                p.url = URL_CHECK_GROUP_PASSWORD;
                p.setContent("password",pwd);
                p.setContent("id",treeID);

                var request = new HttpRequest(p);
                request.onresult = function(error){
                    var securityLevel = this.getNodeValue(XML_SECURITY_LEVEL);
                    var errorInfo = {
                        0:"您输入的密码安全等级为\"不可用\"，不安全",
                        1:"您输入的密码安全等级为\"低\"，只能保障基本安全",
                        2:"您输入的密码安全等级为\"中\"，较安全",
                        3:"您输入的密码安全等级为\"高\"，很安全"
                    };

                    var flag = false;
                    if("0" == securityLevel){
                        alert(errorInfo[securityLevel] + ",请重新输入新密码");
                        
                        //重新打开对话框
                        setTimeout(resetPassword,10);
                    }else if("1" == securityLevel){
                        flag = confirm(errorInfo[securityLevel] + "，仍然要使用吗？");
                    }else{
                        flag = true;
                    }

                    if(true == flag){
                        saveResetPassword(treeID,pwd);
                    }
                }
                request.send();
            }
        }
    }
    /*
     *	保存初始化密码
     *	参数：	string:groupId          组id
                string:password         密码
     *	返回值：
     */
    function saveResetPassword(groupId,password){
        var p = new HttpRequestParams();
        p.url = URL_RESET_PASSWORD;
        p.setContent("groupId",groupId);
        p.setContent("password",password);

        var request = new HttpRequest(p);
        request.onsuccess = function(){
        }
        request.send();
    }
 
 
    /*
     *	检测用户列表右键菜单项是否可见
     *	参数：	string:code     操作码
     *	返回值：
     */
    function getUserOperation(code){
        var flag = false;
        var gridObj = $("grid");
        var curRowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        if(null!=curRowIndex){
            var curRowNode = gridObj.getRowNode_Xml(curRowIndex);
            var _operation = curRowNode.getAttribute("_operation");

            var reg = new RegExp("(^"+code+",)|(^"+code+"$)|(,"+code+",)|(,"+code+"$)","gi");
            if(true==reg.test(_operation)){
                flag = true;
            }
        }
        return flag;
    }
 
    /*
     *	综合查询(用户同步查询)
     *	参数：	
     *	返回值：
     */
    function generalSearchSync(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var groupId = treeNode.getId();
            var groupName = treeNode.getName();
            
            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadGeneralSearchSyncData(groupId);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            inf.defaultPage = "page6";
            inf.label = "用户同步" + OPERATION_SEARCH.replace(/\$label/i,groupName);
            inf.phases = null;
            inf.callback = callback;
            inf.SID = CACHE_GENERAL_SEARCH_SYNC + groupId;
            var tab = ws.open(inf);
        }
    }
    /*
     *	综合查询加载数据
     *	参数：	string:groupId          组id
     *	返回值：
     */
    function loadGeneralSearchSyncData(groupId){
        var cacheID = CACHE_GENERAL_SEARCH_SYNC + groupId;
        var cacheData = Cache.Variables.get(cacheID);
        if(null==cacheData){
            var p = new HttpRequestParams();
            p.url = URL_GENERAL_SEARCH_SYNC;

            p.setContent("groupId", groupId);

            var request = new HttpRequest(p);
            request.onresult = function(){
                var generalSearchGridNode = this.getNodeValue(XML_GENERAL_SEARCH_SYNC);

                var generalSearchGridNodeID = cacheID+"."+XML_GENERAL_SEARCH_SYNC;

                Cache.XmlIslands.add(generalSearchGridNodeID,generalSearchGridNode);

                Cache.Variables.add(cacheID,[generalSearchGridNodeID]);

                initGeneralSearchSyncPages(cacheID);
            }
            request.send();
        }else{
            initGeneralSearchSyncPages(cacheID);
        }
    }
    /*
     *	综合查询相关页加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function initGeneralSearchSyncPages(cacheID){
        var page6GridObj = $("page6Grid");
        Public.initHTC(page6GridObj,"isLoaded","onload",function(){
            loadGeneralSearchSyncGridData(cacheID);
        });
    }
    /*
     *	grid加载数据
     *	参数：	string:cacheID   grid数据相关树节点id
     *	返回值：
     */
    function loadGeneralSearchSyncGridData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_GENERAL_SEARCH_SYNC);
        if(null!=xmlIsland){
            var page6GridObj = $("page6Grid");
            page6GridObj.load(xmlIsland.node,null,"node");
        }
    }
    /*
     *	综合查询(用户授权查询)
     *	参数：	
     *	返回值：
     */
    function generalSearchPermission(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var groupId = treeNode.getId();
            var groupName = treeNode.getName();
            
            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadGeneralSearchPermissionData(groupId);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            inf.defaultPage = "page7";
            inf.label = "用户权限" + OPERATION_SEARCH.replace(/\$label/i,groupName);
            inf.phases = null;
            inf.callback = callback;
            inf.SID = CACHE_GENERAL_SEARCH_PERMISSION + groupId;
            var tab = ws.open(inf);
        }    
    }
    /*
     *	综合查询加载数据
     *	参数：	string:groupId          组id
     *	返回值：
     */
    function loadGeneralSearchPermissionData(groupId){
        var cacheID = CACHE_GENERAL_SEARCH_PERMISSION + groupId;
        var cacheData = Cache.Variables.get(cacheID);
        if(null==cacheData){
            var p = new HttpRequestParams();
            p.url = URL_GENERAL_SEARCH_PERMISSION;

            p.setContent("groupId", groupId);

            var request = new HttpRequest(p);
            request.onresult = function(){
                var generalSearchInfoNode = this.getNodeValue(XML_GENERAL_SEARCH_PERMISSION);

                var generalSearchInfoNodeID = cacheID+"."+XML_GENERAL_SEARCH_PERMISSION;

                Cache.XmlIslands.add(generalSearchInfoNodeID,generalSearchInfoNode);

                Cache.Variables.add(cacheID,[generalSearchInfoNodeID]);

                initGeneralSearchPermissionPages(cacheID);
            }
            request.send();
        }else{
            initGeneralSearchPermissionPages(cacheID);
        }
    }
    /*
     *	综合查询相关页加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function initGeneralSearchPermissionPages(cacheID){
        var page7FormObj = $("page7Form");
        Public.initHTC(page7FormObj,"isLoaded","oncomponentready",function(){
            loadGeneralSearchPermissionFormData(cacheID);            
        });
        loadPermissionListData(cacheID);
    }
    /*
     *	级联资源类型下拉菜单
     *	参数：	string:cacheID              缓存数据id
                string:applicationID        应用id
     *	返回值：
     */
    function updateResourceTypeColumn(cacheID,applicationID){
        var p = new HttpRequestParams();
        p.url = URL_GENERAL_SEARCH_GET_RESOURCETYPES;
        p.setContent("applicationId",applicationID);

        var request = new HttpRequest(p);
        request.onresult = function(){
            var resourceTypeNode = this.getNodeValue(XML_RESOURCE_TYPE);
            var name = resourceTypeNode.getAttribute("name");
            
            var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_GENERAL_SEARCH_PERMISSION);
            if(null!=xmlIsland){
                var oldColumn = xmlIsland.selectSingleNode(".//column[@name='"+name+"']");
                var attributes = resourceTypeNode.attributes;
                for(var i=0,iLen=attributes.length;i<iLen;i++){
                    oldColumn.setAttribute(attributes[i].nodeName,attributes[i].nodeValue);
                }
                loadGeneralSearchPermissionFormData(cacheID);
            }
        }
        request.send();
    }
    /*
     *	xform加载数据
     *	参数：	string:cacheID   grid数据相关树节点id
     *	返回值：
     */
    function loadGeneralSearchPermissionFormData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_GENERAL_SEARCH_PERMISSION);
        if(null!=xmlIsland){
            var page7FormObj = $("page7Form");
            page7FormObj.load(xmlIsland.node,null,"node");

            page7FormObj.ondatachange = function(){
                //2007-3-1 离开提醒
                //attachReminder(cacheID);

                var name = event.result.name;
                var value = event.result.newValue;
                if("applicationId"==name){
                    updateResourceTypeColumn(cacheID,value);
                }
            }
            var page7BtSearch = $("page7BtSearch");
            page7BtSearch.onclick = function(){
                searchPermission(cacheID);
            }
        }
    }
    /*
     *	搜索用户授权信息
     */
    function searchPermission(cacheID){
        var p = new HttpRequestParams();
        p.url = URL_GENERAL_SEARCH_PERMISSION_LIST;

        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_GENERAL_SEARCH_PERMISSION);
        if(null!=xmlIsland){
            var dataNode = xmlIsland.selectSingleNode("./data");
            if(null!=dataNode){
                var prefix = xmlIsland.selectSingleNode("./declare").getAttribute("prefix");
                p.setXFormContent(dataNode,prefix);
            }
        }

        var request = new HttpRequest(p);
        request.onresult = function(){
            var permissionListNode = this.getNodeValue(XML_GENERAL_SEARCH_PERMISSION_LIST);
            var permissionListNodeID = cacheID+"."+XML_GENERAL_SEARCH_PERMISSION_LIST;

            Cache.XmlIslands.add(permissionListNodeID,permissionListNode);
            Cache.Variables.add(cacheID,[permissionListNodeID]);

            loadPermissionListData(cacheID);
        }
        request.send();
    }
    /*
     *	载入授权列表数据
     */
    function loadPermissionListData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_GENERAL_SEARCH_PERMISSION_LIST);
        if(null!=xmlIsland){
            createPermissionList(xmlIsland);
        }else{
            clearPermissionList();
        }
    }
    /*
     *	创建授权列表数据
     *	参数：	XmlNode:xmlIsland       XmlNode实例
     *	返回值：
     */
    function createPermissionList(xmlIsland){
        var page7Box = $("page7Box");
        var str = getPermissionListStr(xmlIsland);
        page7Box.innerHTML = str;
    }
    /*
     *	生成列表html
     *	参数：	XmlNode:xmlIsland       XmlNode实例
     *	返回值：
     */
    function getPermissionListStr(xmlIsland){

        var str = [];
        var userNodes = xmlIsland.selectNodes("./treeNode");

        //遍历用户
        for(var i=0,iLen=userNodes.length;i<iLen;i++){
            var name = userNodes[i].getAttribute("name");
            str[str.length] = "<table class=\"hFull\" border=\"1\" cellspacing=\"0\" cellpadding=\"5\">";
            str[str.length] = "<tr><td colspan=\"4\" style=\"text-decoration:underline;font-weight:bold;\">" + name + "</td></tr>";


            //遍历资源类型以生成表格的行
            var sourceNodes = userNodes[i].selectNodes("./treeNode/treeNode/treeNode");
            for(var j=0,jLen=sourceNodes.length;j<jLen;j++){
                var sourceNode = sourceNodes[j];
                var typeNode = sourceNode.getParent();
                var appNode = typeNode.getParent();


                var name = sourceNode.getAttribute("name");
                str[str.length] = "<tr>";

                if(sourceNode.equals(appNode.getFirstChild().getFirstChild())){
                    var rowspan = appNode.selectNodes("./treeNode/treeNode").length;
                    str[str.length] = "<td rowspan=\"" + rowspan + "\" width=\"10%\">" + appNode.getAttribute("name") + "</td>";                
                }
                if(sourceNode.equals(typeNode.getFirstChild())){
                    var rowspan = typeNode.selectNodes("./treeNode").length;
                    str[str.length] = "<td rowspan=\"" + rowspan + "\" width=\"10%\">" + typeNode.getAttribute("name") + "</td>";
                }
                str[str.length] = "<td width=\"30%\">" + name + "</td>";




                //遍历权限选项
                var optionStr = [];
                var optionNodes = sourceNode.selectNodes("./treeNode");
                for(var k=0,kLen=optionNodes.length;k<kLen;k++){
                    optionStr[optionStr.length] = optionNodes[k].getAttribute("name");
                }
                str[str.length] = "<td width=\"50%\">" + optionStr.join(",") + "</td>";



                str[str.length] = "</tr>";
            }


            str[str.length] = "</table>";
        }

        return str.join("");
    }
    /*
     *	清除授权列表数据
     *	参数：	
     *	返回值：
     */
    function clearPermissionList(){
        var page7Box = $("page7Box");
        page7Box.innerHTML = "";
    }


    /*
     *	综合查询(用户转授查询)
     *	参数：	
     *	返回值：
     */
    function generalSearchReassign(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var groupId = treeNode.getId();
            var groupName = treeNode.getName();
            
            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadGeneralSearchRessignData(groupId);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            inf.defaultPage = "page6";
            inf.label = "用户转授" + OPERATION_SEARCH.replace(/\$label/i,groupName);
            inf.phases = null;
            inf.callback = callback;
            inf.SID = CACHE_GENERAL_SEARCH_REASSIGN + groupId;
            var tab = ws.open(inf);
        }
    }
    /*
     *	综合查询加载数据
     *	参数：	string:groupId          组id
     *	返回值：
     */
    function loadGeneralSearchRessignData(groupId){
        var cacheID = CACHE_GENERAL_SEARCH_REASSIGN + groupId;
        var cacheData = Cache.Variables.get(cacheID);
        if(null==cacheData){
            var p = new HttpRequestParams();
            p.url = URL_GENERAL_SEARCH_REASSIGN;

            p.setContent("groupId", groupId);

            var request = new HttpRequest(p);
            request.onresult = function(){
                var generalSearchGridNode = this.getNodeValue(XML_GENERAL_SEARCH_REASSIGN);

                var generalSearchGridNodeID = cacheID+"."+XML_GENERAL_SEARCH_REASSIGN;

                Cache.XmlIslands.add(generalSearchGridNodeID,generalSearchGridNode);

                Cache.Variables.add(cacheID,[generalSearchGridNodeID]);

                initGeneralSearchReassignPages(cacheID);
            }
            request.send();
        }else{
            initGeneralSearchReassignPages(cacheID);
        }
    }
    /*
     *	综合查询相关页加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function initGeneralSearchReassignPages(cacheID){
        var page6GridObj = $("page6Grid");
        Public.initHTC(page6GridObj,"isLoaded","onload",function(){
            loadGeneralSearchReassignGridData(cacheID);
        });
    }
    /*
     *	grid加载数据
     *	参数：	string:cacheID   grid数据相关树节点id
     *	返回值：
     */
    function loadGeneralSearchReassignGridData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_GENERAL_SEARCH_REASSIGN);
        if(null!=xmlIsland){
            var page6GridObj = $("page6Grid");
            page6GridObj.load(xmlIsland.node,null,"node");
        }
    }
    /*
     *	认证用户组
     */
    function authenticateGroup(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            var treeType = treeNode.getAttribute("groupType");
            var applicationId = treeNode.getAttribute("applicationId");

            var p = new HttpRequestParams();
            p.url = URL_AUTHENTICATE_GROUP;
            p.setContent("groupId",treeID);
            p.setContent("groupType",treeType);
            p.setContent("applicationId",applicationId);
            p.setContent("disabled","0");

            var request = new HttpRequest(p);
            request.onsuccess = function(){
            }
            request.send();
        }
    }
    /*
     *	获取grid操作权限
     *	参数：	number:rowIndex         grid行号
                function:callback       回调函数
     *	返回值：
     */
    function getGridOperation(rowIndex,callback){
        var gridObj = $("grid");
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var id = rowNode.getAttribute("id");
        var _operation = rowNode.getAttribute("_operation");
        var applicationID = gridObj.getXmlDocument().getAttribute("applicationId");
        var groupType = gridObj.getXmlDocument().getAttribute("groupType");
        var groupId = gridObj.getXmlDocument().getAttribute("groupId");
        if("search"==groupId){
            groupId = rowNode.getAttribute("groupId");
        }

        if(null==_operation || ""==_operation){//如果节点上还没有_operation属性，则发请求从后台获取信息
            var p = new HttpRequestParams();
            p.url = URL_GET_USER_OPERATION;
            p.setContent("resourceId",id);
            p.setContent("groupId",groupId);
            p.setContent("applicationId",applicationID);
            p.setContent("groupType",groupType);

            var request = new HttpRequest(p);
            request.onresult = function(){
                _operation = this.getNodeValue(XML_OPERATION);
                rowNode.setAttribute("_operation",_operation);

                if(null!=callback){
                    callback(_operation);
                }
            }
            request.send();
            
        }else{
            if(null!=callback){
                callback(_operation);
            }
        }
    }
    /*
     *	获取树操作权限
     *	参数：	treeNode:treeNode       treeNode实例
                function:callback       回调函数
     *	返回值：
     */
    function getTreeOperation(treeNode,callback){
        var id = treeNode.getId();
        var _operation = treeNode.getAttribute("_operation");
        var applicationId = treeNode.getAttribute("applicationId");
        var groupType = treeNode.getAttribute("groupType");
        var resourceTypeId = treeNode.getAttribute("resourceTypeId");

        if(null==_operation || ""==_operation){//如果节点上还没有_operation属性，则发请求从后台获取信息
            var p = new HttpRequestParams();
            p.url = URL_GET_OPERATION;
            p.setContent("resourceId",id);
            p.setContent("applicationId",applicationId);
            p.setContent("groupType",groupType);
            // 点击其他用户组根节点
            if("-4" == id || "5"==resourceTypeId){
                p.setContent("resourceTypeId","5");
            }

            var request = new HttpRequest(p);
            request.onresult = function(){
                _operation = this.getNodeValue(XML_OPERATION);
                treeNode.setAttribute("_operation",_operation);

                if(null!=callback){
                    callback(_operation);
                }
            }
            request.send();            
        }else{
            if(null!=callback){
                callback(_operation);
            }
        }    
    }
    /*
     *	综合查询(用户角色查询)
     *	参数：	
     *	返回值：
     */
    function generalSearchRole(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var groupId = treeNode.getId();
            var groupName = treeNode.getName();
            
            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadGeneralSearchRoleData(groupId);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            inf.defaultPage = "page6";
            inf.label = "用户角色" + OPERATION_SEARCH.replace(/\$label/i,groupName);
            inf.phases = null;
            inf.callback = callback;
            inf.SID = CACHE_GENERAL_SEARCH_ROLE + groupId;
            var tab = ws.open(inf);
        }
    }
    /*
     *	综合查询加载数据
     *	参数：	string:groupId          组id
     *	返回值：
     */
    function loadGeneralSearchRoleData(groupId){
        var cacheID = CACHE_GENERAL_SEARCH_ROLE + groupId;
        var cacheData = Cache.Variables.get(cacheID);
        if(null==cacheData){
            var p = new HttpRequestParams();
            p.url = URL_GENERAL_SEARCH_ROLE;

            p.setContent("groupId", groupId);

            var request = new HttpRequest(p);
            request.onresult = function(){
                var generalSearchGridNode = this.getNodeValue(XML_GENERAL_SEARCH_ROLE);

                var generalSearchGridNodeID = cacheID+"."+XML_GENERAL_SEARCH_ROLE;

                Cache.XmlIslands.add(generalSearchGridNodeID,generalSearchGridNode);

                Cache.Variables.add(cacheID,[generalSearchGridNodeID]);

                initGeneralSearchRolePages(cacheID);
            }
            request.send();
        }else{
            initGeneralSearchRolePages(cacheID);
        }
    }
    /*
     *	综合查询相关页加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function initGeneralSearchRolePages(cacheID){
        var page6GridObj = $("page6Grid");
        Public.initHTC(page6GridObj,"isLoaded","onload",function(){
            loadGeneralSearchRoleGridData(cacheID);
        });
    }
    /*
     *	grid加载数据
     *	参数：	string:cacheID   grid数据相关树节点id
     *	返回值：
     */
    function loadGeneralSearchRoleGridData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_GENERAL_SEARCH_ROLE);
        if(null!=xmlIsland){
            var page6GridObj = $("page6Grid");
            page6GridObj.load(xmlIsland.node,null,"node");
        }
    }
 
    function setGroupPermission(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var name = treeNode.getName();
            var resourceType = "2";
            var groupType = treeNode.getAttribute("groupType");
            var appType = treeNode.getAttribute("appType");
            var type = "group";
            if(null == groupType){
                if(null != appType){
                    resourceType = "5";
                    type = "app";
                }
            }else{
                if("1" == groupType){// 主用户组
                    resourceType = "2";
                }else if("2" == groupType){// 辅助用户组
                    resourceType = "3";
                }else if("3" == groupType){// 其他用户组
                    resourceType = "4";
                }
            }
            var title = "授予\"" + name + "\"角色";
            var params = {
                roleId:id,
                resourceType:resourceType,
                applicationId:"tss",
                isRole2Resource:"0"
            };
            window.showModalDialog("setpermission.htm",{params:params,title:title,type:type},"dialogWidth:700px;dialogHeight:500px;resizable:yes");
        }
    }
 
    function setUserPermission(){
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var rowNode = gridObj.getRowNode_Xml(rowIndex);

        var id = rowNode.getAttribute("id");
        var name = rowNode.getAttribute("userName");

        var title = "授予\"" + name + "\"角色";
        var params = {
            roleId:id,
            resourceType:"1",
            applicationId:"tss",
            isRole2Resource:"0"
        };

        window.showModalDialog("setpermission.htm",{params:params,title:title,type:"user"},"dialogWidth:700px;dialogHeight:500px;resizable:yes");
    }
 
    /*
     *	将组导入到
     *	参数：	
     *	返回值：
     */
    function importGroupTo(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var groupID = treeNode.getId();
            var groupName = treeNode.getName();
            var groupType = treeNode.getAttribute("groupType");

            //弹出窗口数据从后台获取
            var xmlIsland = null;

            var group = getGroup(groupID,xmlIsland,"导入\""+groupName+"\"到",groupType,"tss","4");
            if(null!=group){

                var p = new HttpRequestParams();
                p.url = URL_IMPORT_GROUP;
                p.setContent("toGroupId",group.id);
                p.setContent("groupId",groupID);
				p.setContent("isCascadeUser",true);

                var request = new HttpRequest(p);
                request.onresult = function(){
					var data = this.getNodeValue("ProgressInfo");
					var progress = new Progress(URL_IMPORT_PROGRESS,data,URL_CANCEL_IMPORT_PROGRESS);
					progress.oncomplete = function(){
						loadInitData();
					}
					progress.start();
                }
                request.send();
            }
        }
    }    
	
    function importUserTo(){
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var userID = rowNode.getAttribute("id");
        var userName = rowNode.getAttribute("userName");
        var loginName = rowNode.getAttribute("loginName");
        var groupType = gridObj.getXmlDocument().getAttribute("groupType");
        var groupID = gridObj.getXmlDocument().getAttribute("groupId");
        if("search"==groupID){
			if(null!=rowIndex){
				var curRowNode = gridObj.getRowNode_Xml(rowIndex);
				groupID = curRowNode.getAttribute("groupId");
			}
        }

		var xmlIsland = null;

		var group = getGroup(groupID,xmlIsland,"导入\""+userName+"\"到",groupType,"tss","4");
		if(null!=group){

			var p = new HttpRequestParams();
			p.url = URL_IMPORT_USER;
			p.setContent("groupId", groupID);
			p.setContent("toGroupId",group.id);
			p.setContent("userId",userID);

			var request = new HttpRequest(p);
			request.onsuccess = function(){
				gridObj.modifyNamedNode_Xml(rowIndex,"appUserName",userName);
				gridObj.modifyNamedNode_Xml(rowIndex,"appGroupName",group.name);
				gridObj.modifyNamedNode_Xml(rowIndex,"appLoginName",loginName);
			}
			request.send();
		}

    }
 
    function isOtherUser(){
        var flag = false;
        var gridObj = $("grid");
        var groupType = gridObj.getXmlDocument().getAttribute("groupType");
        if("3" == groupType){
            flag = true;
        }
        return flag;
    }

 
    function syncUser(){
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var rowName = gridObj.getNamedNodeValue_Xml(rowIndex,"userName");
        var rowId = rowNode.getAttribute("id");
        var groupId = gridObj.getXmlDocument().getAttribute("groupId");
        if("search"==groupId){
            groupId = rowNode.getAttribute("groupId");
        }
        var groupType = gridObj.getXmlDocument().getAttribute("groupType");
        var applicationId = gridObj.getXmlDocument().getAttribute("applicationId");

        var p = new HttpRequestParams();
        p.url = URL_SYNC_USER;

        p.setContent("applicationId",applicationId);
        p.setContent("groupId",groupId);
        p.setContent("groupType",groupType);
        p.setContent("userId",rowId);

        var request = new HttpRequest(p);
        request.onsuccess = function(){
            //同步成功
        }
        request.send();

    }
 
    function getUserGroupType(){
        var gridObj = $("grid");
        var groupType = gridObj.getXmlDocument().getAttribute("groupType");
        return groupType;
    }



    window.onload = init;

	//关闭页面自动注销
    logoutOnClose();