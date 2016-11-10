from django.conf.urls import url, include
from django.contrib import admin
from . import views

from django.conf.urls import handler404

urlpatterns = [
    ## Registeration and Signin
    url(r'^$', views.signin, name='signin'),
    url(r'^signup$', views.signup, name='signup'),  # for registering new users
    url(r'^signout$', views.signout, name='signout'),
    url(r'^complete-reg$', views.completeReg, name='complete-reg'),
    url(r'^complete-signup$',views.complete_signup,name="complete_signup"),

    ## View and register Students
    ## Only for Admin :(
    url(r'^students/view$', views.view_students, name="view_students"),
    url(r'^students/add$', views.add_students, name="add_students"),

    ## home sweet home
    url(r'^home$', views.home, name='home'),

    ## Courses relatedstuff
    url(r'^courses$', views.view_courses, name='view_course'),
    url(r'^courses/add$', views.add_courses, name='add_course'),
    url(r'^courses/(?P<pk>\d+)/$', views.course_detail, name='course_detail'),

    # Feedback related stuff
    url(r'^feedback/add/(?P<course_id>\d+)/$',
        views.add_feedback,
        name="add_feedback"),
    url(r'^feedback/view/(?P<pk>\d+)/$',
        views.view_feedback,
        name="view_feedback"),
    url(r'^feedback/add/$', views.add_feedback_all, name="add_feedback_all"),
    url(r'^feedback/view/$', views.view_feedback_all,
        name="view_feedback_all"),

    ## Assignments related stuff
    url(r'^assignments$', views.assigns, name='view_assignment'),
    url(r'^assignments/add$', views.add_assigns, name='add_assignment'),
    url(r'^assignments/view/(?P<pk>\d+)/$',
        views.view_assign,
        name='view_assign_info'),

	## Mobile API
	url(r'^api/login$', views.login_api, name='login_api'),
	url(r'^api/signout$', views.signout_api, name='signout_api'),
	url(r'^api/courses$', views.course_list_api, name='courses_api'),
    # url(r'api/dates$', views.dates, name='dates')
]


handler404 = 'website.views.handler404'