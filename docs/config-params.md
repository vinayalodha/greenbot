# Config Params

Config params are represent by JSON on home page 

```text
[
    {
        "key": "excluded_tag",
        "value": "",
        "description": "Resources with this tag will not be analyzed. Format <key>=<value>"
    },
    {
        "key": "included_tag",
        "value": "",
        "description": "Only resources with this tag will be analyzed. Format <key>=<value>"
    },
    {
        "key": "too_many_ami_threshold",
        "value": "5000",
        "description": "Threshold AMI count above which TooManyInstanceImagesRule will raise concern"
    }
]
```

These config parameters are used to tweak behavior of rules.

