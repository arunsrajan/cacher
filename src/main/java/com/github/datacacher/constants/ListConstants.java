package com.github.datacacher.constants;

public class ListConstants {
    public static final String listUrlPath = "/list";
    public static enum LISTCOMMANDS{GET,GETALL,PUT,PUTALL,CREATE,DELETE,DELETEALL,DELETE_ENTERIES,DELETE_ENTRIES_ALL};
    public static final String LISTREQUEST = "LIST_REQUEST";
    public static final String LIST = "list";
    public static final String MANDATORYFIELDLISTNAME = "Please provide mandatory field list name";
    public static final String EMPTYLISTCANNOTEADDED = "List cannot be empty for adding values";
    public static final String EMPTYLISTCANNOTEREMOVED = "List cannot be empty for removing values";
    public static final String LISTAVAILABLE = "List cannot be create, list already available";
    public static final String NULLLIST = "Please provide List for update";
    public static final String LISTTOBECREATED = "Please create list";
    public static final String LISTNOTAVAILABLEFORDELETION = "List Not available for deletion, List not created";
    public static final String LISTNOTAVAILABLEFORVALUESDELETION = "List Not available for deleting values, List not created";
    public static final String LISTNAME = "listName";
    public static final String ADD = "/add";
    public static final String VALUES = "/values";
    public static final String SORT = "/sortget";
}
