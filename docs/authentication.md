# Authentication Module Specification
Path: auth/

## Module Description
Used to authenticate the user, issue the JWT.

## Definition of APIs
### Login
Path: auth/login

Methods: POST

#### Parameters
|Parameter|Description|
|------|--------|
|username|Username.|
|password|Password.|

#### Returns
|Field|Description|
|-------|-------|
|error: int|Error code.|
|desc: String| The description of current state.|
|token: String| The JWT, exists only if the authentication successed.|

### Renew
Refresh the token.

Path: auth/renew

Method: POST

Need authentication.
#### Parameters
(None)

#### Returns
|Field|Description|
|----|------|
|error: int|Error code.|
|token: String| The renewed JWT.|

#### Behaviors
1. Only issues the new JWT if the old JWT is going to expire, any other case lead to reject without any description.
