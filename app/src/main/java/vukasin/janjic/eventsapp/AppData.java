package vukasin.janjic.eventsapp;

import java.util.ArrayList;

public class AppData {

    public static ArrayList<Event> allEvents = new ArrayList<Event>();
    public static ArrayList<Event> interestedEvents = new ArrayList<Event>();
    public static ArrayList<Event> attendingEvents = new ArrayList<Event>();

    static {
        Event e1 = EventFactory.createPromotedEvent(
                "EXIT Festival",
                "Najveci muzicki festival u regionu.",
                "Petrovaradin, Novi Sad",
                "15.07.2026 18:00",
                "Festival",
                R.drawable.ic_launcher_foreground,
                50000
        );

        Event e2 = EventFactory.createPromotedEvent(
                "NEON Party",
                "Veliki promoted party.",
                "Stark Arena, Beograd",
                "10.05.2026 22:00",
                "Party",
                R.drawable.ic_launcher_foreground,
                200
        );

        Event e3 = EventFactory.createRegularEvent(
                "Rooftop Summer Party",
                "Letnja zurka na krovu.",
                "Dorcol Platz, Beograd",
                "20.06.2026 21:00",
                "Party",
                R.drawable.ic_launcher_foreground
        );

        Event e4 = EventFactory.createRegularEvent(
                "Beer Fest",
                "Festival piva i muzike.",
                "Usce, Beograd",
                "12.08.2026 16:00",
                "Festival",
                R.drawable.ic_launcher_foreground
        );

        Event e5 = EventFactory.createRegularEvent(
                "Nikola Djuricko: Monodrama",
                "Pozorisna monodrama.",
                "Narodno pozoriste, Beograd",
                "03.05.2026 20:00",
                "Stand-Up & Theater",
                R.drawable.ic_launcher_foreground
        );

        Event e6 = EventFactory.createRegularEvent(
                "Hamlet",
                "Pozorisna predstava.",
                "JDP, Beograd",
                "01.03.2026 19:30",
                "Stand-Up & Theater",
                R.drawable.ic_launcher_foreground
        );

        Event e7 = EventFactory.createRegularEvent(
                "Filharmonija: Beethoven",
                "Koncert klasicne muzike.",
                "Kolarac, Beograd",
                "11.05.2026 19:00",
                "Concert",
                R.drawable.ic_launcher_foreground
        );

        Event e8 = EventFactory.createRegularEvent(
                "Konstrakta Live",
                "Koncert uzivo.",
                "Novi Sad",
                "20.01.2026 20:00",
                "Concert",
                R.drawable.ic_launcher_foreground
        );

        Event e9 = EventFactory.createRegularEvent(
                "Foto Beograd 2026",
                "Izlozba savremene fotografije.",
                "Galerija Haos, Beograd",
                "14.05.2026 11:00",
                "Exhibition",
                R.drawable.ic_launcher_foreground
        );

        Event e10 = EventFactory.createRegularEvent(
                "Modern Art Expo",
                "Izlozba moderne umetnosti.",
                "MSU, Beograd",
                "18.06.2026 10:00",
                "Exhibition",
                R.drawable.ic_launcher_foreground
        );

        Event e11 = EventFactory.createRegularEvent(
                "Street Musicians Festival",
                "Festival ulicnih muzicara.",
                "Knez Mihajlova, Beograd",
                "25.05.2026 12:00",
                "Festival",
                R.drawable.ic_launcher_foreground
        );

        Event e12 = EventFactory.createRegularEvent(
                "Beach Party Palic",
                "Letnja zurka na Palicu.",
                "Palic, Subotica",
                "05.08.2026 20:00",
                "Party",
                R.drawable.ic_launcher_foreground
        );

        Event e13 = EventFactory.createRegularEvent(
                "Laki Stand-Up Specijal",
                "Vece stand-up komedije.",
                "Dom omladine, Beograd",
                "15.02.2026 20:00",
                "Stand-Up & Theater",
                R.drawable.ic_launcher_foreground
        );

        Event e14 = EventFactory.createRegularEvent(
                "Jazz Night Nis",
                "Vece jazz muzike.",
                "Niska tvrdjava, Nis",
                "10.08.2025 19:00",
                "Concert",
                R.drawable.ic_launcher_foreground
        );

        Event e15 = EventFactory.createRegularEvent(
                "Science Fair",
                "Naucna izlozba i prezentacije.",
                "Sajam, Novi Sad",
                "22.09.2026 09:00",
                "Exhibition",
                R.drawable.ic_launcher_foreground
        );

        Event e16 = EventFactory.createRegularEvent(
                "Wine & Music Evening",
                "Spoj vina i muzike.",
                "Sremski Karlovci",
                "12.12.2026 18:30",
                "Party",
                R.drawable.ic_launcher_foreground
        );

        Event e17 = EventFactory.createRegularEvent(
                "Food Expo",
                "Gastro manifestacija.",
                "Sajam, Beograd",
                "30.10.2026 12:00",
                "Exhibition",
                R.drawable.ic_launcher_foreground
        );

        Event e18 = EventFactory.createRegularEvent(
                "Indie Rock Night",
                "Koncert alternativne muzike.",
                "KC Grad, Beograd",
                "05.11.2026 21:00",
                "Concert",
                R.drawable.ic_launcher_foreground
        );

        allEvents.add(e1);
        allEvents.add(e2);
        allEvents.add(e3);
        allEvents.add(e4);
        allEvents.add(e5);
        allEvents.add(e6);
        allEvents.add(e7);
        allEvents.add(e8);
        allEvents.add(e9);
        allEvents.add(e10);
        allEvents.add(e11);
        allEvents.add(e12);
        allEvents.add(e13);
        allEvents.add(e14);
        allEvents.add(e15);
        allEvents.add(e16);
        allEvents.add(e17);
        allEvents.add(e18);

        interestedEvents.add(e1);
        interestedEvents.add(e3);
        interestedEvents.add(e4);
        interestedEvents.add(e7);
        interestedEvents.add(e10);

        attendingEvents.add(e1);
        attendingEvents.add(e5);
        attendingEvents.add(e6);
        attendingEvents.add(e7);
        attendingEvents.add(e8);
        attendingEvents.add(e9);
        attendingEvents.add(e12);
        attendingEvents.add(e14);
    }

    public static ArrayList<Event> getSortedEvents() {
        ArrayList<Event> sortedEvents = new ArrayList<Event>();

        for (Event event : allEvents) {
            if (event.isPromoted()) {
                sortedEvents.add(event);
            }
        }

        for (Event event : allEvents) {
            if (!event.isPromoted()) {
                sortedEvents.add(event);
            }
        }

        return sortedEvents;
    }

    public static ArrayList<Event> getSortedEventsByCategory(String category) {
        ArrayList<Event> filteredEvents = new ArrayList<Event>();

        for (Event event : allEvents) {
            if (event.getCategory().equals(category)) {
                filteredEvents.add(event);
            }
        }

        ArrayList<Event> sortedFilteredEvents = new ArrayList<Event>();

        for (Event event : filteredEvents) {
            if (event.isPromoted()) {
                sortedFilteredEvents.add(event);
            }
        }

        for (Event event : filteredEvents) {
            if (!event.isPromoted()) {
                sortedFilteredEvents.add(event);
            }
        }

        return sortedFilteredEvents;
    }

    public static Event findByName(String name) {
        for (Event event : allEvents) {
            if (event.getName().equals(name)) {
                return event;
            }
        }
        return null;
    }
}