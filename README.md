# Reading Progress Service


### Api

1. Open Plan


### How to Run

Open Plan:
```shell
# 204 NO CONTENT - OK
http PUT localhost:8080/reading-progress/1e8f503d-dfce-4a4b-ae7c-e51f2d5daa67 \
planId=f9cfecb1-a608-429c-bad4-2a976dcce4ce \
readerId=cc9da6cd-80a1-4fb9-9aa4-8cb32b7eef9c 
```

```shell
# 409 CONFLICT if you execute after the above request
http PUT localhost:8080/reading-progress/0e8f503d-dfce-4a4b-ae7c-e51f2d5daa67 \
planId=f9cfecb1-a608-429c-bad4-2a976dcce4ce \
readerId=cc9da6cd-80a1-4fb9-9aa4-8cb32b7eef9c 
```


```shell
# 400 - BAD REQUEST
http PUT localhost:8080/reading-progress/0e8f503d-dfce-4a4b-ae7c-e51f2d5daa67 \
planId=invalid-plan-id \
readerId=cc9da6cd-80a1-4fb9-9aa4-8cb32b7eef9c 
```

```yaml
Response:
  HTTP Status:
    204 NO CONTENT if open or re-open a plan
    400 BAD REQUEST
    409 CONFLICT
```