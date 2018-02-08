# Description

## Intelligence Tips API
    url: http://[ip]:[port]/bi/query
    queryParameter: none
    request: 
        data format: text/plain
        method: get
        description: part of geo chinese name
        example: 湖
    response:
        data format: application/json
        description: relative data for the request body data
        example: {
                   "code": "1000",
                   "msg": "success",
                   "data": [
                     {
                       "id": "98K",
                       "level": 5,
                       "name": "湖北省",
                       "parent": "中国"
                     },
                     {
                       "id": "awm",
                       "level": 5,
                       "name": "湖南省",
                       "parent": "中国"
                     }
                   ]
                 }
                 
## Geo Data Merge API             
    url：http://[ip]:[port]/bi/merge
    queryParameter: none
    request:
        data format: application/json
        method: post
        description: colloction of geo datas
        example:{
                  "ids": ["98k", "kks", "chn-hun"]
                }
    response:
        data format: application/json
        description: result
        example: {
                   "code": "1000",
                   "msg": "success"
                 }