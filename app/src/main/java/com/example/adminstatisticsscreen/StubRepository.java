package com.example.adminstatisticsscreen;

import androidx.constraintlayout.widget.ConstraintAttribute;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.example.adminstatisticsscreen.Kid;
import com.example.adminstatisticsscreen.Parent;
public class StubRepository {
    long DAY_IN_MS = 1000 * 60 * 60 * 24;
    ArrayList<Kid> kids ;
    ArrayList<Parent> parents ;
    ArrayList<Meeting> meetings ;
    ArrayList<Course> courses ;
    ArrayList<Category> categories ;
   private static StubRepository stub_instance;
    public static StubRepository getInstance() {
        if (stub_instance == null)
            stub_instance = new StubRepository();
        return stub_instance;
    }
    private StubRepository(){
        kids = new ArrayList<Kid>();
        parents = new ArrayList<Parent>();
        meetings = new ArrayList<Meeting>();
        courses = new ArrayList<Course>();
        categories = new ArrayList<Category>();

////////////////   Generating Data
        generateData();
    }

    private void generateData(){
        for (int i =0; i<500; i++){
            int month;
            int day;
            Gender gender;
            if(i/10 >11){
                month = i/100;
            }else{
                month = i/10;
            }
            if(i/10>28 ){
                day= i/100;
            }else{
                day = i/10;
            }
            if(i%2 == 0){
                gender=Gender.Boy;
            }else{
                gender=Gender.Girl;
            }

            parents.add(new Parent(UUID.randomUUID().toString(), "parent" + String.valueOf(i),generatePhoneNum(),"email"+String.valueOf(i)+"@gmail.com", "password"+ String.valueOf(i)));
            kids.add(new Kid(UUID.randomUUID().toString(),"kid"+String.valueOf(i),new Date(randomNum(90,121),month,day),gender,parents.get(randomNum(0,parents.size()-1)).getId()));
           if(i<4){
               categories.add(new Category(UUID.randomUUID().toString(),"category"+String.valueOf(i)));
           }
            courses.add(new Course(UUID.randomUUID().toString(), "course"+String.valueOf(i),new Date(randomNum(120,121),randomNum(0,11),randomNum(0,28)),categories.get(randomNum(0,categories.size()-1)).getId() ));
           for(int j = 0; j<5; j++){
               boolean cancelled = false;
               if(j*4>15){
                   cancelled=true;
               }
               meetings.add(new Meeting(courses.get(randomNum(0,courses.size()-1)).getID(),new Date(randomNum(120,121),randomNum(0,11),randomNum(0,28)),j/2,cancelled));
           }

           kids.get(i).setActiveDate(new Date(randomNum(120,121),randomNum(0,11), randomNum(0,28)));
            parents.get(i).setActiveDate(new Date(randomNum(120,121),randomNum(0,11), randomNum(0,28)));
//add course
        }
    }
private String generatePhoneNum(){
    Random rand = new Random();
    int num1 = (rand.nextInt(7) + 1) * 100 + (rand.nextInt(8) * 10) + rand.nextInt(8);
    int num2 = rand.nextInt(743);
    int num3 = rand.nextInt(10000);

    DecimalFormat df3 = new DecimalFormat("000"); // 3 zeros
    DecimalFormat df4 = new DecimalFormat("0000"); // 4 zeros

    String phoneNumber = df3.format(num1) + "-" + df3.format(num2) + "-" + df4.format(num3);
return phoneNumber;
}
    public  List getDatesBetween(Date startDate, Date endDate) {
        List datesInRange = new ArrayList<>();
        Calendar calendar = getCalendarWithoutTime(startDate);
        Calendar endCalendar = getCalendarWithoutTime(endDate);

        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            datesInRange.add(result);
            calendar.add(Calendar.DATE, 1);
        }

        return datesInRange;
    }

    private  Calendar getCalendarWithoutTime(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }
    private int randomNum(int min , int max){
        if (min > max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
    public void attachCoursesKids(){
        for(Kid k : kids){
            Course course = courses.get(randomNum(0,courses.size()-1));
            course.addKid(k.getId());
            k.addCourse(course.getID());
        }
    }

    ////////////// CHARTS STUBS DEPENDING ON ABOVE GENERATED DATA
    public int getNewKids(int period){
        int c =0;
        Date d ;
        if(period == 1) {
            d = new Date((new Date()).getTime()- 7*DAY_IN_MS);
        }else if(period == 2) {
            d = new Date((new Date()).getTime()-35*DAY_IN_MS);
        }
        else {
            d = new Date((new Date()).getTime()- 365*DAY_IN_MS);
        }
        for( Kid k : kids) {
            if(  k.getStatus().equals(Status.Active) && k.getActiveDate().after(d)) {
                    c++;
            }
        }
        return c;
    }

    public int getNewParents(int period){
        int c =0;
        Date d ;
        if(period == 1) {
            d = new Date((new Date()).getTime()- 7*DAY_IN_MS);
        }else if(period == 2) {
            d = new Date((new Date()).getTime()-35*DAY_IN_MS);
        }
        else {
            d = new Date((new Date()).getTime()- 365*DAY_IN_MS);
        }
        for( Parent p : parents) {
            if(  p.getStatus().equals(Status.Active) && p.getActiveDate().after(d)) {
                c++;
            }
        }
        return c;
    }



public HashMap<String ,Double> getActivityTime(int period){
        Date d;
    if(period == 1) {
        d = new Date((new Date()).getTime()- 7*DAY_IN_MS);
    }else if(period == 2) {
        d = new Date((new Date()).getTime()-35*DAY_IN_MS);
    }
    else {
        d = new Date((new Date()).getTime()- 365*DAY_IN_MS);
    }
    double totalTime = 0;
    double doneTime = 0;

    for(Meeting meeting : meetings) {
        if(meeting.getMeetingDateTime().after(d)) {
            totalTime += meeting.getActualMeetingDuration();
            if(!meeting.isCancelled()) {
                doneTime += meeting.getActualMeetingDuration();
            }
        }
    }
    HashMap<String, Double> toReturn = new HashMap<String,Double>();
    toReturn.put("totalTime", totalTime-doneTime);
    toReturn.put("activityTime", doneTime);
    return toReturn;
}

public HashMap<String, Integer> getKidsCountByCategory(int period){
        HashMap<String,Integer> toReturn = new HashMap<>();
    Date d;
    if(period == 1) {
        d = new Date((new Date()).getTime()- 7*DAY_IN_MS);
    }else if(period == 2) {
        d = new Date((new Date()).getTime()-35*DAY_IN_MS);
    }
    else {
        d = new Date((new Date()).getTime()- 365*DAY_IN_MS);
    }
    HashMap<String, Integer> kidsCountByCategory = new HashMap<String, Integer>();
    for(String catId : getAllCategoriesIds()) {
        int categoryKids = 0;
        ArrayList<Course> courses = getCategoryCourses(catId);
        for(Course c : courses) {
            if(c.getStartDateTime().after(d)) {
                categoryKids += c.getKidsIDs().size();
            }
        }

        kidsCountByCategory.put(getCategoryById(catId).getName(), categoryKids);
    }
    return kidsCountByCategory;
}

private ArrayList<String> getAllCategoriesIds(){
        ArrayList<String> res = new ArrayList<String>();
        for (Category c : categories){
            res.add(c.getId());
        }
        return res;
}
private Category getCategoryById(String id){
    for (Category c : categories){
        if(c.getId().equals(id)){
            return c;
        }
    }
    return null;
}
private ArrayList<Course>  getCategoryCourses(String id){
    ArrayList<Course> res = new ArrayList<Course>();
    for (Course c : courses){
        if(c.getCategoryId().equals(id)){
            res.add(c);
        }
    }
    return res;
}
}
