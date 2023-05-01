(function ($) {
  "use strict";

  // Setup the calendar with the current date
  $(document).ready(function () {
    var date = new Date();
    // Set click handlers for DOM elements
    $(".right-button").click({ date: date }, next_year);
    $(".left-button").click({ date: date }, prev_year);
    $(".month").click({ date: date }, month_click);
    // Set current month as active
    $(".months-row").children().eq(date.getMonth()).addClass("active-month");
    init_calendar(date);
  });

  // Initialize the calendar by appending the HTML dates
  function init_calendar(date) {
    $(".tbody").empty();
    var calendar_days = $(".tbody");
    var month = date.getMonth();
    var year = date.getFullYear();
    var day_count = days_in_month(month, year);
    var row = $("<tr class='table-row'></tr>");
    var today = date.getDate();
    // Set date to 1 to find the first day of the month
    date.setDate(1);
    var first_day = date.getDay();
    // 35+firstDay is the number of date elements to be added to the dates table
    // 35 is from (7 days in a week) * (up to 5 rows of dates in a month)
    for (var i = 0; i < 35 + first_day; i++) {
      // Since some of the elements will be blank,
      // need to calculate actual date from index
      var day = i - first_day + 1;

      // If it is a sunday, make a new row
      if (i % 7 === 0) {
        calendar_days.append(row);
        row = $("<tr class='table-row'></tr>");
      }
      // if current index isn't a day in this month, make it blank
      if (i < first_day || day > day_count) {
        var curr_date = $("<td class='table-date nil'>" + "</td>");
        row.append(curr_date);
      } else {
        var curr_date = $("<td class='table-date'>" + day + "</td>");
        if (today === day && $(".active-date").length === 0) {
          curr_date.addClass("active-date");
        }
        // If this date has any events, style it with .event-date
        if (hasAgenda(day, month + 1, year)) {
          console.log("Ada Agenda");
          curr_date.addClass("event-date");
        }
        // Set onClick handler for clicking a date
        curr_date.click({ month: months[month], day: day }, date_click);
        row.append(curr_date);
      }
    }
    // Append the last row and set the current year
    calendar_days.append(row);
    $(".year").text(year);
    set_date();
  }

  // Get the number of days in a given month/year
  function days_in_month(month, year) {
    var monthStart = new Date(year, month, 1);
    var monthEnd = new Date(year, month + 1, 1);
    return (monthEnd - monthStart) / (1000 * 60 * 60 * 24);
  }

  // Event handler for when a date is clicked
  function date_click() {
    $(".active-date").removeClass("active-date");
    $(this).addClass("active-date");
    set_date();
  }

  // Event handler for when a month is clicked
  function month_click(event) {
    var date = event.data.date;
    $(".active-month").removeClass("active-month");
    $(this).addClass("active-month");
    var new_month = $(".month").index(this);
    date.setMonth(new_month);
    init_calendar(date);
    set_date();
  }

  // Event handler for when the year right-button is clicked
  function next_year(event) {
    var date = event.data.date;
    var new_year = date.getFullYear() + 1;
    $("year").html(new_year);
    date.setFullYear(new_year);
    init_calendar(date);
    set_date();
  }

  // Event handler for when the year left-button is clicked
  function prev_year(event) {
    var date = event.data.date;
    var new_year = date.getFullYear() - 1;
    $("year").html(new_year);
    date.setFullYear(new_year);
    init_calendar(date);
    set_date();
  }

  function set_date() {
    let year = document.querySelector(".year").innerHTML;
    let month = getMonth(document.querySelector(".active-month").innerHTML);
    let day = getDay(document.querySelector(".active-date").innerHTML);

    function getMonth(month) {
      if (month == "Jan") {
        return "01";
      }
      if (month == "Feb") {
        return "02";
      }
      if (month == "Mar") {
        return "03";
      }
      if (month == "Apr") {
        return "04";
      }
      if (month == "May") {
        return "05";
      }
      if (month == "Jun") {
        return "06";
      }
      if (month == "Jul") {
        return "07";
      }
      if (month == "Aug") {
        return "08";
      }
      if (month == "Sep") {
        return "09";
      }
      if (month == "Oct") {
        return "10";
      }
      if (month == "Nov") {
        return "11";
      }
      if (month == "Dec") {
        return "12";
      }
    }

    function getDay(day) {
      if (day.length == 1) {
        return `0${day}`;
      } else {
        return day;
      }
    }

    document.getElementById("agendaDate").value = `${year}-${month}-${day}`;
  }

  // Checks if a specific date has any agendas
  function hasAgenda(day, month, year) {
    if (globalEvent == undefined || null == globalEvent) return false;
    var retval = false;
    var dateWithAgenda = globalEvent.split("#");
    for (var i = 0; i < dateWithAgenda.length; i++) {
      var dateSegments = dateWithAgenda[i].split("_");
      var d = dateSegments[0];
      var m = dateSegments[1];
      var y = dateSegments[2];

      if (d == day && m == month && y == year) {
        retval = true;
        break;
      }
    }

    return retval;
  }

  const months = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ];
})(jQuery);
