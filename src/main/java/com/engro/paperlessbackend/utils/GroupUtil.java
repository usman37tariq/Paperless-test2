package com.engro.paperlessbackend.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class GroupUtil {

	public List<String> getListOfNewMembersAddedInGroup(List<String> existingGroupUsers,
			List<String> groupUserIdsFromUI) {
		List<String> newAddedGroupMemebersIds = new ArrayList<String>();

		for (String id : groupUserIdsFromUI) {

			if (!existingGroupUsers.contains(id)) {
				newAddedGroupMemebersIds.add(id);
			}
		}

		return newAddedGroupMemebersIds;
	}
	
	public List<String> getListOfMembersRemovedFromGroup(List<String> existingGroupUsers,
			List<String> groupUserIdsFromUI) {
		List<String> removedGroupMemebersIds = new ArrayList<String>();

		for (String id : existingGroupUsers) {

			if (!groupUserIdsFromUI.contains(id)) {
				removedGroupMemebersIds.add(id);
			}
		}

		return removedGroupMemebersIds;
	}
	
}
