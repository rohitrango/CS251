from django.shortcuts import render, reverse, get_object_or_404
from django.http import HttpResponse, HttpResponseRedirect
from django.contrib.auth.decorators import login_required
from django.contrib.auth import logout, authenticate, login
from .models import *
from django.contrib import messages
from django.views.decorators.csrf import csrf_exempt
from django.utils.decorators import method_decorator
from django.core import serializers
import json
from urllib import request as urlreq
# Create your views here.
from datetime import datetime as dt
import datetime
# noinspection PyUnresolvedReferences
from ratelimit.decorators import ratelimit

## Show the main view
## More like index page
@ratelimit(key='ip', rate='100/h')
def signin(request):
    if request.method == "GET":
        user = request.user
        if user.is_authenticated():
            return HttpResponseRedirect(reverse('website:home'))

        return render(request, "signin.html")

    elif request.method == "POST":

        username = request.POST['username']
        password = request.POST['password']

        user = authenticate(username=username, password=password)
        if user is None:
            messages.add_message(request, messages.ERROR, 'Invalid Username/Password.')
            return render(request, 'signin.html')

        member = user.member
        if member.mtype == 'ST':
            messages.add_message(request, messages.ERROR, 'Student logins are not allowed.')
            return render(request, 'signin.html')

        login(request, user)
        return HttpResponseRedirect(reverse('website:home'))


## Signup view which takes data and creates the account
@ratelimit(key='ip', rate='100/h')
def signup(request):

    if request.method == "GET":
        return HttpResponseRedirect(reverse('website:signin'))
    elif request.method == "POST":
        try:
            fullname = request.POST['fullname']
            username = request.POST['sUsername']
            password = request.POST['sPassword']
            isProf = request.POST.get('prof')
        except:
            messages.add_message(request, messages.ERROR, 'Invalid details!')
            return HttpResponseRedirect(reverse('website:signin'))

        ## We have the data
        userexists = User.objects.filter(username=username)
        if len(userexists) != 0:
            messages.add_message(request, messages.ERROR, "Email already used. Please use a different email.")
            return HttpResponseRedirect(reverse('website:signin'))

        newuser = User.objects.create(username=username, email=username)
        newuser.set_password(password)
        newuser.save()

        if isProf == "on":
            mtype = "PR"
        else:
            mtype = "TA"

        newmember = Member.objects.create(
            user=newuser,
            fullname=fullname,
            mtype=mtype, )
        newmember.save()
        messages.add_message(request, messages.SUCCESS, 'Successfully registered! Now you can log in.')
        return HttpResponseRedirect(reverse('website:signin'))


## Add views for Home Page
## Will contain different stuff for Admins and TAs/Profs
@login_required
def home(request):
    ## Anon users are shooed away
    if request.user.is_anonymous():
        messages.add_message(request, messages.ERROR, 'Please login first!')
        return HttpResponseRedirect(reverse('website:signin'))

    if request.user.is_superuser:
        return HttpResponseRedirect('/admin')

    user = request.user
    member = get_object_or_404(Member, user=user)

    context = {'member': member}

    return render(request, 'home.html', context)


## Signout and return to homepage
def signout(request):
    logout(request)
    return HttpResponseRedirect(reverse('website:signin'))


## Complete Registration
## Completes facebook signup
@ratelimit(key='ip', rate='100/h')
def completeReg(request):

    if request.method == "POST":
        print(request)
        try:
            token = request.POST['access_token']
        except:
            messages.add_message(request, messages.ERROR, 'Invalid access.')
            return HttpResponseRedirect(reverse('website:home'))

        ## Token has been retrieved
        print(token)

        ## Use Facebook ID to store things
        url = "https://graph.facebook.com/v2.8/me?fields=first_name,middle_name,last_name&access_token="+token
        content = json.loads((urlreq.urlopen(url).read()).decode('utf-8'))

        print(content)

        ## if user exists, log in
        try:
            user = User.objects.filter(username=content['id'])
            if len(user) != 0:
                user = user[0]
                login(request,user)
                return HttpResponseRedirect(reverse('website:home'))


            context = {
                'fullname': content['first_name'] + " " + content['last_name'],
                'username': content['id'],
            }
            
        except:
            messages.add_message(request, messages.ERROR, 'Invalid access token!')
            return HttpResponseRedirect(reverse('website:home'))


        return render(request,'complete-reg.html',context)

    else:
        messages.add_message(request, messages.ERROR, 'Invalid details!')
        return HttpResponseRedirect(reverse('website:home'))


## Complete signup here
@ratelimit(key='ip', rate='100/h')
def complete_signup(request):

    if request.method == "POST":
        # response from form
        try:
            fullname = request.POST['fullname']
            fb_id = request.POST['username']
            username = request.POST['username']
            isProf = request.POST.get('prof')
        except:
            messages.add_message(request, messages.ERROR, 'Invalid details!')
            return HttpResponseRedirect(reverse('website:signin'))

        user = User.objects.filter(username=fb_id)
        if len(user) != 0:
            print(user)
            login(request,user)
            return HttpResponseRedirect(reverse('website:signin'))

        user = User.objects.create(username=fb_id)
        user.save()

        if isProf == "on":
            mtype = "PR"
        else:
            mtype = "TA"

        member = Member.objects.filter(user=user)
        if len(member) != 0:
            user.delete()
            messages.add_message(request, messages.ERROR, 'This user already exists! Try logging in!')
            return HttpResponseRedirect(reverse('website:signin'))

        member = Member.objects.create(
            user=user, fullname=fullname, mtype=mtype)
        member.save()
        messages.add_message(request, messages.SUCCESS, 'The account is successfully created.')
        return HttpResponseRedirect(reverse('website:home'))

    else:
        messages.add_message(request, messages.ERROR, 'Invalid access.')
        return HttpResponseRedirect(reverse('website:home'))


#######################################################################################
### Admin features
### View and add students
@login_required
def view_students(request):

    member = Member.objects.get(user=request.user)
    if member.mtype != "AD":
        return render(request, 'view_students.html',{
            'error':'Only admin(s) can view the list.'
        })

    students = Member.objects.filter(mtype="ST").order_by('fullname')
    context = {
        'students':students,
    }
    return render(request, 'view_students.html',context)

# Add student 
@login_required
def add_students(request):

    member = Member.objects.get(user=request.user)
    if member.mtype != "AD":
        return render(request, 'add_students.html',{
            'error':'Only admin(s) can view the list.',
            'hide':True
        })

    if request.method == "GET":
        return render(request, 'add_students.html')

    else:
        
        checkSingle = request.POST.get('fullname')

        ## Single register
        if checkSingle is not None:

            fullname = request.POST['fullname']
            username = request.POST['rollnumber'] + "@iitb.ac.in"
            test = User.objects.filter(username=username)
            if len(test) != 0:
                return render(request, 'add_students.html',{
                    'error':'This roll number already exists. Please try again.',
                })

            password = request.POST['password']
            u = User.objects.create(
                username=username,
                )

            u.set_password(password)
            u.save()

            m = Member.objects.create(
                user=u,
                fullname=fullname,
                mtype="ST",
                )

            messages.add_message(request, messages.SUCCESS, 'Successfully added student!')
            return HttpResponseRedirect(reverse('website:view_students'))

        else:
            try:
                print(request.FILES)
                studentdata = request.FILES['students']
                ## read data
                lines = studentdata.readlines()
                lines = list(map(lambda x: x.decode("utf-8"), lines))
                lines = list(map(lambda x: x[0:-1].split(','), lines))
                lines = list(map(lambda x: [x[0],x[1] + "@iitb.ac.in", x[2]], lines))

                for line in lines:
                    if len(line[2]) < 8:
                        messages.add_message(request, messages.ERROR, 'Password must be at least 8 characters long. Please check the file again.')
                        return HttpResponseRedirect(reverse('website:add_students'))
            except:
                messages.add_message(request, messages.ERROR, 'Incorrect format of submitted file!')
                return HttpResponseRedirect(reverse('website:add_students'))

            # Filter the line
            lines = list(filter(lambda x: len(User.objects.filter(username=x[1]))==0, lines))
            print(lines)
            print("\n\n\n\n")

            users = list(map(lambda x: User(username=x[1]), lines))
            for index in range(len(users)):
                users[index].set_password(lines[index][2])
            # Set all passwords

            User.objects.bulk_create(users)

            members = []
            for line in lines:
                members.append(Member(
                    user=User.objects.get(username=line[1]),
                    mtype="ST",
                    fullname=line[0],
                ))

            Member.objects.bulk_create(members)
            ### Send response
            messages.add_message(request, messages.SUCCESS, 'Student list is being added! It may take a few moments.')
            # return HttpResponse(request)
            return HttpResponseRedirect(reverse('website:add_students'))


######################################################################################s
#     Course page
#    View courses, add courses and show details for courses 
### View courses, all courses for admins, Selected courses for Profs, or TAs
######################################################################################
@login_required
def view_courses(request):

    member = Member.objects.get(user=request.user)

    ## Admin, bring up all the courses
    if member.mtype == "AD":
        all_courses = Course.objects.all().order_by('-added')
        if (len(all_courses) == 0):
            return render(request, 'view_courses.html',
                          {'error': 'No courses to view.'})
        try:
            profs = [
                course.members.filter(mtype="PR")[0] for course in all_courses
            ]
        except:
            return render(request, 'view_courses.html',
                          {'error': 'No courses to view.'})

        return render(request, 'view_courses.html', {
            'all_courses': all_courses,
            'member': member,
            'profs': profs
        })
    ## Prof or TA, bring up the courses of the prof or TA
    else:
        all_courses = member.course_set.all()
        return render(request, 'view_courses.html',
                      {'all_courses': all_courses,
                       'member': member})


### Add courses for Admins
@login_required
def add_courses(request):
    member = Member.objects.get(user=request.user)
    if request.method == "GET":
        profs = Member.objects.filter(mtype="PR")
        tas = Member.objects.filter(mtype="TA")
        students = Member.objects.filter(mtype="ST")

        context = {
            'mtype': member.mtype,
            'profs': profs,
            'students': students,
            'tas': tas,
        }

        return render(request, 'add_courses.html', context)

    elif request.method == "POST":

        print(request.POST)

        try:
            course_name = request.POST['name']
            code = request.POST['code']
            semester = int(request.POST['semester'])
        except:
            messages.add_message(request, messages.ERROR, 'Invalid details!')
            return HttpResponse(request)

        course = Course.objects.create(
            name=course_name, course_code=code, semester=semester)

        prof = request.POST.get('prof')
        students = request.POST.getlist('student')
        tas = request.POST.getlist('ta')
        print(prof, students, tas)

        prof = Member.objects.get(user__username=prof)
        course.members.add(prof)

        for ta in tas:
            taObj = Member.objects.get(user__username=ta)
            course.members.add(taObj)

        for student in students:
            stuObj = Member.objects.get(user__username=student)
            course.members.add(stuObj)

        course.save()

        ## Save the midsem Feedback now
        ## Copied from add_feedback, remember to change this in case of bugs
        ##########################################################################################################################

        deadlineM = (request.POST['deadlineM'])
        deadlineM = datetime.datetime.strptime(deadlineM,'%d %B, %Y')

        myfeedback = Feedback.objects.create(
            name=request.POST['feedbacknameM'],
            course=course,
            deadline=deadlineM)
        myfeedback.save()

        ## Get the rating questions
        rating_q = request.POST.getlist('RquestionM')
        for r in rating_q:
            newRatingQuestion = FeedbackQuestion.objects.create(
                question=r,
                feedback=myfeedback,
                q_type="RATE", )
            newRatingQuestion.save()

            # Give the rating answers, 5 instances 
            for i in range(5):
                rating = FeedbackRatingAnswer.objects.create(
                    q=newRatingQuestion,
                    rating=(i + 1), )
                rating.save()

        ## Get the Short Questions
        short_q = request.POST.getlist('SquestionM')
        if short_q is not None:
            for question in short_q:
                shortQuestion = FeedbackQuestion.objects.create(
                    question=question,
                    feedback=myfeedback,
                    q_type='SHORT', )
                shortQuestion.save()

        ## Get the MCQ questions
        ## Tricky
        mcq_question = request.POST.getlist('MquestionM')
        if mcq_question is not None:
            counter = 0  # Counter for m_answer
            q_no = 0  # Number of questions
            MAnswer = request.POST.getlist('MAnswerM')
            NoOptions = request.POST.getlist('NoOptionsM')

            ## Add question and add options
            for question in mcq_question:
                m_q = FeedbackQuestion.objects.create(
                    question=question,
                    feedback=myfeedback,
                    q_type='MCQ', )
                m_q.save()

                ## Add options
                options = int(NoOptions[q_no])
                for i in range(options):
                    addOption = FeedbackMCQChoice.objects.create(
                        text=MAnswer[counter],
                        q=m_q, )
                    addOption.save()
                    counter += 1

                q_no += 1

        ########################################################################################################################## 

        ## Save the midsem Feedback now
        ## Copied from add_feedback, remember to change this in case of bugs
        ##########################################################################################################################

        deadlineE = datetime.datetime.strptime((request.POST['deadlineE']),'%d %B, %Y')

        myfeedback = Feedback.objects.create(
            name=request.POST['feedbacknameE'],
            course=course,
            deadline=deadlineE)
        myfeedback.save()

        ## Get the rating questions
        rating_q = request.POST.getlist('RquestionE')
        for r in rating_q:
            newRatingQuestion = FeedbackQuestion.objects.create(
                question=r,
                feedback=myfeedback,
                q_type="RATE", )
            newRatingQuestion.save()

            # Give the rating answers, 5 instances 
            for i in range(5):
                rating = FeedbackRatingAnswer.objects.create(
                    q=newRatingQuestion,
                    rating=(i + 1), )
                rating.save()

        ## Get the Short Questions
        short_q = request.POST.getlist('SquestionE')
        if short_q is not None:
            for question in short_q:
                shortQuestion = FeedbackQuestion.objects.create(
                    question=question,
                    feedback=myfeedback,
                    q_type='SHORT', )
                shortQuestion.save()

        ## Get the MCQ questions
        ## Tricky
        mcq_question = request.POST.getlist('MquestionE')
        if mcq_question is not None:
            counter = 0  # Counter for m_answer
            q_no = 0  # Number of questions
            MAnswer = request.POST.getlist('MAnswerE')
            NoOptions = request.POST.getlist('NoOptionsE')

            ## Add question and add options
            for question in mcq_question:
                m_q = FeedbackQuestion.objects.create(
                    question=question,
                    feedback=myfeedback,
                    q_type='MCQ', )
                m_q.save()

                ## Add options
                options = int(NoOptions[q_no])
                for i in range(options):
                    addOption = FeedbackMCQChoice.objects.create(
                        text=MAnswer[counter],
                        q=m_q, )
                    addOption.save()
                    counter += 1

                q_no += 1

        ########################################################################################################################## 
        ## Store the midsems and endsems exams now

        name = request.POST['enamem']
        desc = request.POST.get('edescm')
        deadline = datetime.datetime.strptime((request.POST['edeadlinem']),'%d %B, %Y')

        assignment = Assignment.objects.create(
            name=name, description=desc, deadline=deadline, course=course)
        assignment.save()

        ## Store the endsems now

        name = request.POST['enamee']
        desc = request.POST.get('edesce')
        deadline = datetime.datetime.strptime((request.POST['edeadlinee']),'%d %B, %Y')

        assignment = Assignment.objects.create(
            name=name, description=desc, deadline=deadline, course=course)
        assignment.save()

        return HttpResponseRedirect(reverse("website:view_course"))


@login_required
def course_detail(request, pk):

    member = Member.objects.get(user=request.user)
    course = get_object_or_404(Course, pk=pk)

    if member.mtype == "AD":
        feedbacks = course.feedback_set.all()
        assignments = course.assignment_set.all()
        courseprof = course.members.filter(mtype="PR")[0]

    else:
        if course not in member.course_set.all():
            return render(request, 'course_detail.html', {
                'error':
                "Sorry, this is not your course, you aren't allowed to see it."
            })

        feedbacks = course.feedback_set.all()
        assignments = course.assignment_set.all()
        courseprof = None

    context = {
        'feedbacks': feedbacks,
        'member': member,
        'course': course,
        'assignments': assignments,
        'courseprof': courseprof,
        'students_enrolled': len(course.members.filter(mtype="ST")),
        'ta_count': len(course.members.filter(mtype="TA")),
        'now': dt.now(),
    }

    return render(request, 'course_detail.html', context)


#################################################################################################
#     Feedback pages - Add feedback, view feedback replies
#     Anything else? 
#################################################################################################
@login_required
def add_feedback(request, course_id):

    member = Member.objects.get(user=request.user)

    if member.mtype == "AD":
        return render(
            request, 'add_feedback.html',
            {'error': 'Professors and TAs will now handle feedbacks.'})

    else:
        # GET request, render stuff here
        if request.method == "GET":
            course = get_object_or_404(Course, pk=course_id)
            if course not in member.course_set.all():
                return render(request, 'add_feedback.html', {
                    'error':
                    'This is not your course. You cannot add a feedback form for the same.'
                })

            context = {
                "course": course,
                "member": member,
            }
            return render(request, 'add_feedback.html', context)

        # POST request sent, add the feedback here
        elif request.method == "POST":
            print(request.POST)
            # return HttpResponse("Not added")

            course = Course.objects.get(pk=int(request.POST['course_id']))
            deadline = datetime.datetime.strptime((request.POST['deadline']),'%d %B, %Y')

            myfeedback = Feedback.objects.create(
                name=request.POST['feedbackname'],
                course=course,
                deadline=deadline)
            myfeedback.save()

            ## Get the rating questions
            rating_q = request.POST.getlist('Rquestion')

            for r in rating_q:
                newRatingQuestion = FeedbackQuestion.objects.create(
                    question=r,
                    feedback=myfeedback,
                    q_type="RATE", )
                newRatingQuestion.save()

                # Give the rating answers, 5 instances 
                for i in range(5):
                    rating = FeedbackRatingAnswer.objects.create(
                        q=newRatingQuestion,
                        rating=(i + 1), )
                    rating.save()

            ## Get the Short Questions
            short_q = request.POST.getlist('Squestion')
            if short_q is not None:
                for question in short_q:
                    shortQuestion = FeedbackQuestion.objects.create(
                        question=question,
                        feedback=myfeedback,
                        q_type='SHORT', )
                    shortQuestion.save()

            ## Get the MCQ questions
            ## Tricky
            mcq_question = request.POST.getlist('Mquestion')
            if mcq_question is not None:
                counter = 0  # Counter for m_answer
                q_no = 0  # Number of questions
                MAnswer = request.POST.getlist('MAnswer')
                NoOptions = request.POST.getlist('NoOptions')

                ## Add question and add options
                for question in mcq_question:
                    m_q = FeedbackQuestion.objects.create(
                        question=question,
                        feedback=myfeedback,
                        q_type='MCQ', )
                    m_q.save()

                    ## Add options
                    options = int(NoOptions[q_no])
                    for i in range(options):
                        addOption = FeedbackMCQChoice.objects.create(
                            text=MAnswer[counter],
                            q=m_q, )
                        addOption.save()
                        counter += 1

                    q_no += 1

            return HttpResponseRedirect(reverse('website:home'))

        else:
            messages.add_message(request, messages.ERROR, 'Bad request!')
            return HttpResponse(request)


@login_required
def view_feedback(request, pk):

    member = Member.objects.get(user=request.user)

    if member.mtype == "AD":
        return render(
            request, 'add_feedback.html',
            {'error': 'Professors and TAs will now handle feedbacks.'})

    else:
        # GET request, render stuff here
        if request.method == "GET":
            feedback = get_object_or_404(Feedback, pk=pk)
            course = feedback.course

            if course not in member.course_set.all():
                return render(request, 'view_feedback.html',
                              {'error': 'This is not your course.'})

            rating_q = feedback.feedbackquestion_set.filter(q_type="RATE")
            short_q = feedback.feedbackquestion_set.filter(q_type="SHORT")
            mcq_q = feedback.feedbackquestion_set.filter(q_type="MCQ")

            rating = []
            for q in rating_q:
                rating.append({
                    'question': q,
                    'answers': q.feedbackratinganswer_set.all(),
                })

            short = []
            if len(short_q) != 0:
                for q in short_q:
                    short.append({
                        'question': q,
                        'answers': q.feedbackshortanswer_set.all(),
                    })

            mcq = []
            if len(mcq_q) != 0:
                for q in mcq_q:
                    mcq.append({
                        'question': q,
                        'answers': q.feedbackmcqchoice_set.all(),
                    })

            context = {
                'feedback': feedback,
                'course': course,
                'rating': rating,
                'short': short,
                'mcq': mcq,
                'now': dt.now(),
            }

            return render(request, 'view_feedback.html', context)


### Add feedbacks considering all courses.
@login_required
def add_feedback_all(request):
    member = Member.objects.get(user=request.user)

    if member.mtype == "AD":
        return render(
            request, 'add_feedback_all.html',
            {'error': 'Professors and TAs will now handle feedbacks.'})

    else:
        # GET request, render stuff here
        if request.method == "GET":

            all_courses = member.course_set.all()
            if len(all_courses) == 0:
                return render(request, 'add_feedback_all.html',
                              {'error': 'You are not taking any courses.'})

            context = {
                'all_courses': all_courses,
                'member': member,
            }

            return render(request, 'add_feedback_all.html', context)

        else:
            return HttpResponseRedirect(reverse('website:home'))


### View all feedbacks by courses!
@login_required
def view_feedback_all(request):

    member = Member.objects.get(user=request.user)

    if member.mtype == "AD":
        return render(
            request, 'view_feedback_all.html',
            {'error': 'Professors and TAs will now handle feedbacks.'})

    else:
        # GET request, render stuff here
        if request.method == "GET":

            all_courses = member.course_set.all()
            if len(all_courses) == 0:
                return render(request, 'view_feedback_all.html',
                              {'error': 'You are not taking any courses.'})

            course_data = []
            for course in all_courses:
                course_data.append({
                    'course': course,
                    'feedbacks': course.feedback_set.all(),
                })

            context = {
                'course_data': course_data,
                'member': member,
                'now':dt.now(),
            }

            return render(request, 'view_feedback_all.html', context)

        else:
            return HttpResponseRedirect(reverse('website:home'))


########################################################################################################
#### Assignments


## Display all assignments of all courses of the prof/TA
@login_required()
def assigns(request):
    member = Member.objects.get(user=request.user)

    if member.mtype == "AD":
        return render(request, 'view_assignments.html', {
            "error":
            "Trespassers will be shot. Survivors will be shot again :P"
        })

    all_courses = member.course_set.all()
    assignments = [
    ]  # 2D array ; each element contains all assignments of a subject

    single_course_assign = []
    zero = True
    for course in all_courses:
        assign_for_course = Assignment.objects.filter(course=course)

        for assign in assign_for_course:
            single_course_assign.append(assign)

        if len(single_course_assign) != 0:
            zero = False
        assignments.append(single_course_assign)
        single_course_assign = []

    if zero:
        return render(request, 'view_assignments.html',
                      {"error": "No assignments for now."})
    context = {'courses': all_courses, 'assigns': assignments, 'now':dt.now()}
    return render(request, 'view_assignments.html', context)


## Allows prof and TAs to add assignments
@login_required()
def add_assigns(request):
    if request.method == "GET":
        member = Member.objects.get(user=request.user)

        if member.mtype == "AD":
            return render(
                request, 'add_assignments.html',
                {'error': 'Professors and TAs handle course assignments.'})

        all_courses = member.course_set.all()

        if len(all_courses) == 0:
            return render(request, 'add_assignments.html',
                          {'error': 'You are not taking any course.'})

        return render(request, 'add_assignments.html',
                      {'courses': all_courses})

    elif request.method == "POST":
        member = Member.objects.get(user=request.user)

        if member.mtype == "AD":
            return render(
                request, 'add_assignments.html',
                {'error': 'Professors and TAs handle course assignments.'})

        name = request.POST['name']
        desc = request.POST.get('desc')
        deadline = datetime.datetime.strptime((request.POST['deadline']),'%d %B, %Y')
        course_code = request.POST['course']
        course = Course.objects.get(course_code=course_code)

        assignment = Assignment.objects.create(
            name=name, description=desc, deadline=deadline, course=course)
        assignment.save()

        return HttpResponseRedirect(reverse('website:view_assignment'))


## View informations of a specific assingment
@login_required
def view_assign(request, pk):
    user = request.user
    member = Member.objects.get(user=user)

    if member.mtype == "AD":
        return render(request, 'view_assignment_info.html', {
            "error":
            "Trespassers will be shot. Survivors will be shot again :P"
        })

    if request.method == "GET":
        assign = get_object_or_404(Assignment, pk=pk)
        # assign = Assignment.objects.get(pk=pk)
        # believe me, they are going to put in random values of assignment Primary keys
        course = assign.course
        member_list = course.members.all()
        if member in member_list:
            return render(request, 'view_assignment_info.html',
                          {'assign': assign,"now":dt.now()})

        else:
            return render(request, 'view_assignment_info.html',
                          {'error': "Sorry you are not taking this course."})


## HTTP 404 request handler
from django.shortcuts import render_to_response
from django.template import RequestContext


def handler404(request):
    response = render_to_response('404.html', {},
                                  context_instance=RequestContext(request))
    response.status_code = 404
    return response


########################################################################################################
##################### Website Ends Here. Mobile API begins


## Checks if token is correct
## This function wll return a member or None (if there is no member)
@method_decorator(csrf_exempt, name='checkstud')
def stud_check(request):
    token = request.POST.get('token')
    if token is None:
        # print("No token")
        return None
    try:
        member = Member.objects.get(token=token)
    except:
        # print("Something is wrong here")
        return None

    if member.mtype != "ST":
        # print("Not a student")
        return None

    return member


## Signin for mobile app
@method_decorator(csrf_exempt, name='loginapi')
def login_api(request):

    if request.method == "POST":
        try:
            username = request.POST['username']
            password = request.POST['password']
        except:
            return HttpResponse("-1")

        user = authenticate(username=username, password=password)

        if user is None:
            return HttpResponse("-1")

        member = Member.objects.get(user=user)
        if member.mtype != "ST":
            return HttpResponse("-1")

        # generate the Token
        import random,string
        token = "".join([random.choice(string.ascii_letters + string.digits) for n in range(32)])
        member.token = token
        member.save()

        response = serializers.serialize('json',[member],fields=('fullname','token'))
        print(response)

        return HttpResponse(response)

    elif request.method == "GET":
        return HttpResponse("-1")


# Logout view
@method_decorator(csrf_exempt,name="signoutapi")
def signout_api(request):
    try:
        token = request.POST['token']
        member = Member.objects.get(token=token)
        if token == "-1":
            return HttpResponse("-1")
        member.token = "-1"
        member.save()
        return HttpResponse("0")
    except:
        return HttpResponse("-1")


## Specific course detials
@method_decorator(csrf_exempt, name='courselistapi')
def course_list_api(request):
    if request.method == "POST":
        member = stud_check(request)
        print(member)
        if member is None:
            return HttpResponse("-1")
        try:
            c_pk = request.POST['course_id']
            course = Course.objects.get(pk=c_pk)
        except:
            return HttpResponse("-1")

        course_json = json.loads(
            serializers.serialize('json', [course], fields=('name', 'semester', 'added', 'course_code')))
        course_json[0]['feedbacks'] = json.loads(serializers.serialize('json', course.feedback_set.all()))
        course_json[0]['assignments'] = json.loads(serializers.serialize('json', course.assignment_set.all()))

        return HttpResponse(json.dumps(course_json))
    else:
        return HttpResponse("-1")

@method_decorator(csrf_exempt, name='allcoursesapi')
def all_courses_api(request):
    if request.method == "POST":
        member = stud_check(request)
        print(member)
        if member is None:
            return HttpResponse("-1")
        course_list = member.course_set.all()
        json_list = serializers.serialize('json', course_list, fields=('name', 'course_code'))

        return HttpResponse(json_list)
    else:
        return HttpResponse("-1")


## List all the dates (feedback and assignment deadlines)
@method_decorator(csrf_exempt,name="dates")
def dates_api(request):
    if request.method == "POST":
        member = stud_check(request)
        if member is None:
            return HttpResponse("-1")
        course_list = member.course_set.all()

        json_data = {}
        completed_feedbacks = []
        incomplete_feedbacks = []
        assignments = []


        for course in course_list:
            for feedback in course.feedback_set.all():
                if member in feedback.students.all():
                    completed_feedbacks.append(feedback)
                else:
                    incomplete_feedbacks.append(feedback)

            assignments += course.assignment_set.all()

        json_data['assignment'] = json.loads(serializers.serialize('json', assignments))
        json_data['complete'] = json.loads(serializers.serialize('json', completed_feedbacks))
        json_data['incomplete'] = json.loads(serializers.serialize('json', incomplete_feedbacks))

        print(json_data)
        return HttpResponse(json.dumps(json_data))

    else:
        return HttpResponse("-1")


## Get all the feedbacks of the course
## Get course ID to get all its feedbacks and assignments
@method_decorator(csrf_exempt,name="course_deadlines_api")
def course_deadlines_api(request):
    print(request)
    if request.method == "POST":
        member = stud_check(request)
        if member is None:
            return HttpResponse("-1")
        try:
            c_pk = request.POST['course_id']
            course = Course.objects.get(pk=c_pk)
        except:
            return HttpResponse("-1")

        json_data = {}
        completed_feedbacks = []
        incomplete_feedbacks = []
        assignments = []

        now = dt.now()
        for feedback in course.feedback_set.filter(deadline__gt=now):
            if member in feedback.students.all():
                completed_feedbacks.append(feedback)
            else:
                incomplete_feedbacks.append(feedback)

        assignments += course.assignment_set.filter(deadline__gt=now)

        json_data['assignment'] = json.loads(serializers.serialize('json', assignments))
        json_data['complete'] = json.loads(serializers.serialize('json', completed_feedbacks))
        json_data['incomplete'] = json.loads(serializers.serialize('json', incomplete_feedbacks))

        return HttpResponse(json.dumps(json_data))
    else:
        return HttpResponse("-1")


## Give all the assignment details
@method_decorator(csrf_exempt,name="assignment_detail")
def assignment_detail(request):
    if request.method == "POST":
        member = stud_check(request)
        if member is None:
            return HttpResponse("-1")
        try:
            assignment_id = request.POST['pk']
            assignment = Assignment.objects.filter(pk=assignment_id)
            json_data = serializers.serialize('json',assignment)
            return HttpResponse(json_data)
        except:
            return HttpResponse("-1")
    else:
        return HttpResponse("-1")


## Send form
@method_decorator(csrf_exempt,name="course_form_api")
def course_form_api(request):
    if request.method == "POST":
        member = stud_check(request)
        if member is None:
            return HttpResponse("-1")
        try:
            f_id = request.POST["feedback_id"]
            c_pk = request.POST['course_id']
            course = Course.objects.get(pk=c_pk)
            feedback = Feedback.objects.get(course=course, pk=f_id)
            ques = feedback.feedbackquestion_set.all()
            json_data = serializers.serialize('json',ques)
            return HttpResponse(json_data)
        except:
            return HttpResponse("-1")
    else:
        return HttpResponse("-1")

## Receive feedback response


### Send the form and save it
@method_decorator(csrf_exempt,name="submit_feedback_form")
def submit_feedback_form(request):
    if request.method == "POST":
        member = stud_check(request)
        if member is None:
            return HttpResponse("-1")
        feedback_id = request.POST['feedback_id']
        feedback = Feedback.objects.get(pk=int(feedback_id))
        # Parse all the answers
        short_ans = []

        # Questions
        questions = feedback.feedbackquestion_set.all()
        for question in questions:
            key = question.pk
            val = request.POST[str(key)]
            if question.q_type == "SHORT":
                short_ans.append(
                    FeedbackShortAnswer(q=question,text=val)
                )
            elif question.q_type == "RATE":
                ans = FeedbackRatingAnswer.objects.get(q=question,rating=int(val))
                ans.votes += 1
                ans.save()

            elif question.q_type == "MCQ":
                ans = FeedbackMCQChoice.objects.get(pk=int(val))
                ans.votes_count+=1
                ans.save()

        FeedbackShortAnswer.objects.bulk_create(short_ans)
        feedback.students.add(member)
        return HttpResponse("{'message':'Feedback successfully recorded.'}")

    else:
        return HttpResponse("-1")



## Send form from API to app
## PLEASE DONT CHANGE THIS
@method_decorator(csrf_exempt,name="course_form_api")
def course_form_api(request):
    if request.method == "POST":
        member = stud_check(request)
        if member is None:
            return HttpResponse("-1")
        # try:
        f_id = int(request.POST["feedback_id"])
        feedback = Feedback.objects.get(pk=f_id)

        rate_ques = feedback.feedbackquestion_set.filter(q_type="RATE")
        short_ques = feedback.feedbackquestion_set.filter(q_type="SHORT")
        mcq_ques = feedback.feedbackquestion_set.filter(q_type="MCQ")

        rate_ques = json.loads(serializers.serialize('json',rate_ques))
        short_ques = json.loads(serializers.serialize('json',short_ques))
        mcq_ques = json.loads(serializers.serialize('json',mcq_ques))

        json_obj = {}
        json_obj['rate_ques'] = rate_ques
        json_obj['short_ques'] = short_ques
        json_obj['mcq_ques'] = mcq_ques

        for i in range(len(json_obj['mcq_ques'])):
            q = FeedbackQuestion.objects.get(pk=json_obj['mcq_ques'][i]['pk'])
            options = q.feedbackmcqchoice_set.all()
            options = json.loads(serializers.serialize('json',options))
            json_obj['mcq_ques'][i]['options'] = options

        json_obj['mcq_ques'] = mcq_ques

        return HttpResponse(json.dumps(json_obj))
        # except:
        # return HttpResponse("-1")
    else:
        return HttpResponse("-1")