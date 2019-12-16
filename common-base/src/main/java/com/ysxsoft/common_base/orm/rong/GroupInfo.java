package com.ysxsoft.common_base.orm.rong;

import com.ysxsoft.common_base.base.BaseApplication;
import com.ysxsoft.common_base.utils.SharedPreferencesUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class GroupInfo extends DataSupport {
    public long id;
    private String groupName;
    private String groupId;
    private String uid;
    private String groupIcon;

    public static void saveGroup(String groupId, String groupName, String groupIcon) {
        GroupInfo friends = new GroupInfo();
        friends.uid = SharedPreferencesUtils.getUid(BaseApplication.getContext());
        friends.groupId = groupId;
        friends.groupName = groupName;
        friends.groupIcon = groupIcon;
        friends.saveOrUpdate("groupId=? and uid=?", groupId, SharedPreferencesUtils.getUid(BaseApplication.getContext()));
    }

    public static GroupInfo getGroup(String groupId) {
        GroupInfo group = DataSupport.where("groupId=? and uid=?", groupId,SharedPreferencesUtils.getUid(BaseApplication.getContext())).findLast(GroupInfo.class);
        return group;
    }

    public static void updateGroup(String groupId, String groupName, String groupIcon) {
        GroupInfo group = GroupInfo.getGroup(groupId);
        group.groupName = groupName;
        group.groupIcon = groupIcon;
        group.update(group.id);
    }

    public static void deleteGroup(String groupId) {
        GroupInfo group = GroupInfo.getGroup(groupId);
        if (group != null) {
            group.delete();
        }
    }

    public static boolean clearAllGroup() {
        return DataSupport.deleteAll(GroupInfo.class) > 0;
    }

    public static List<GroupInfo> getGroupList() {
        List<GroupInfo> group = DataSupport.findAll(GroupInfo.class);
        if (group == null) {
            group = new ArrayList<>();
        }
        return group;
    }

    public static boolean exist(String groupId) {
        return GroupInfo.getGroup(groupId) == null;
    }

    public static boolean checkGroupIconChanged(String groupId, String iconUrl) {
        if (GroupInfo.exist(groupId)) {
            GroupInfo group = GroupInfo.getGroup(groupId);
            if (group != null && group.groupIcon != null && !group.groupIcon.equals(iconUrl)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 优先返回备注
     *
     * @param groupId
     * @return
     */
    public static String getGroupName(String groupId) {
        String groupName = "";
        GroupInfo groupInfo = GroupInfo.getGroup(groupId);
        if (groupInfo != null && groupInfo.groupName != null) {
            groupName = groupInfo.groupName;
        }
        return groupName;
    }
}
