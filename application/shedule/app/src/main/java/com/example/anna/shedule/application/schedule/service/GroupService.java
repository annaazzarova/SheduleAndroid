package com.example.anna.shedule.application.schedule.service;

import com.example.anna.shedule.application.schedule.model.Group;
import com.example.anna.shedule.application.schedule.model.StaticLesson;
import com.example.anna.shedule.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.anna.shedule.application.database.Database.getDbInstance;

public class GroupService {

    public List<StaticLesson> mapGroupsOnLessons(List<StaticLesson> lessons) {
        Set<String> setOfGroupIds = new HashSet<>();
        Map<Long, List<String>> lessonIdToGroupIds = new HashMap<>();
        Map<String, Group> groupIdToGroup = new HashMap<>();

        for (StaticLesson lesson: lessons) {
            String groupIds = lesson.getGroupIds();
            if (groupIds != null) {
                List<String> ids = StringUtils.split(groupIds, ',');
                lessonIdToGroupIds.put(lesson.getId(), ids);
                setOfGroupIds.addAll(ids);
            }
        }

        List<Group> groups = getGroupsByIds(setOfGroupIds);
        for (Group group: groups) {
            if (group.getGroupId() != null) {
                groupIdToGroup.put(group.getGroupId(), group);
            }
        }

        for (StaticLesson lesson: lessons) {
            List<String> groupIds = lessonIdToGroupIds.get(lesson.getId());
            mapGroupsOnLesson(lesson, groupIds, groupIdToGroup);
        }

        return lessons;
    }

    private void mapGroupsOnLesson(StaticLesson lesson, List<String> groupIds, Map<String, Group> groupIdToGroup) {
        if (groupIds == null) return;
        List<Group> groups = new ArrayList<>(groupIds.size());
        for (String groupId: groupIds) {
            Group group = groupIdToGroup.get(groupId);
            if (group != null) {
                groups.add(group);
            }
        }
        lesson.setGroups(groups);
    }

    public List<Group> getGroupsByIds(Collection<String> groupIds) {
        if (groupIds.isEmpty()) return Collections.emptyList();

        String groupIdsAsString = StringUtils.join(groupIds, "', '");
        String query = "SELECT * FROM " + Group.TABLE_NAME + " WHERE groupId in ('" + groupIdsAsString + "')";
        return getDbInstance().getByQuery(Group.class, query);
    }

}
