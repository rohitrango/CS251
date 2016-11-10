from django import template
register = template.Library()


def index(List, i):
    return List[int(i)]

register.filter('index',index)

### Cite -> http://stackoverflow.com/questions/4651172/reference-list-item-by-index-within-django-template