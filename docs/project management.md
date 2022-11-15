# Project Management Specification
The APIs for project managing.

## Module Description
These API handles the CRUD of Project (Translation Project) itself. Including the configuration and administration.

Should be implemented RESTful.

All of these interfaces need to check the user's authorization.

## Submodules 
1. group: CRUD of the group member(s).
2. admin: Authorization configuration of group member(s).
3. content: Project contents management (CRUD, import/export, etc.).

## API Definition
The specification of this API.

Path: proj/proj

### Create

#### Parameters
|Parameter|Description|
|---------|-----------|
|name: String| The name of the project that need to be create.|
|type: int| The type of the project. 0 for public, 1 for private.|
|~~tags: String~~| The tags given to the project.|

#### Returns
|Field|Description|
|---------|-----------|
|id: int| The id of the created project.|
|error: int| The error code to corresponding query.|
|desc: String| The description of the error.|

### Read
#### Parameters
|Parameter|Description|
|-----|-------|
|id: int| The project id of specified project.|

#### Returns
|Parameter|Description|
|---------|-----------|
|error: int| Error code.|
|desc: String| The description of error code.|
|data: Project| The object of queried project. Can be omitted if query failed.|

#### Behaviors
1. If the project is private and the user have no permission to access corresponding project, the project should not be returned.

### Update
#### Parameters
|Parameter|Description|
|----|------|
|id: int| The id of the project.|
|data: Project| The new project object. Specified all new data.|
#### Returns
|Field|Description|
|----|------|
|error: int| Error code.|
|desc: String| The description of the error code.|

#### Behaviors
1. If the user have no permission to make any change on the project, this API should reject the query and provide error code.
2. If the specified project id does not exisit, acts as Create does (ignore the id field in query).
3. If it do update, and some fields provided in "data" is not able to be changed, ignore invalid fields and return as normal.

### Query
Implemented with GET, no id required.

Special path: proj/search

#### Parameters
|Parameter|Description|
|---------|-----------|
|name: String| Name of the project need to be found.|
|ownerName: String| Name of the project owner.|
|owner: int| The user id of the project owner.|
|~~tags: String~~| Name of the tag which is attached to the wanted project.|

#### Returns
|Field|Description|
|-----|------|
|error: int| Error code.|
|data: Object[]| See following.|

data is an array of object(s) defined as:
|Field|Description|
|------|------|
|id: int| The id of matched project.|
|name: String| The name of matched project.|
|tags: String| The name of matched project.|
|owner: int| The user id of the owner.|

#### Behaviors
1. If the project is private and the querier have no permission to access the project, the project should not be returned.

## Data Structures Involved
### Project
This data structure is using to record any data used in the project.
