package hu.gde.runnersdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/runner")
public class RunnerRestController {

    @Autowired
    private LapTimeRepository lapTimeRepository;
    private RunnerRepository runnerRepository;

    private ShoeNameRepository shoeNameRepository;

    @Autowired
    public RunnerRestController(RunnerRepository runnerRepository, LapTimeRepository lapTimeRepository, ShoeNameRepository shoeNameRepository) {
        this.runnerRepository = runnerRepository;
        this.lapTimeRepository = lapTimeRepository;
        this.shoeNameRepository = shoeNameRepository;
    }

    @GetMapping("/{id}")
    public RunnerEntity getRunner(@PathVariable Long id) {
        return runnerRepository.findById(id).orElse(null);
    }

    @GetMapping("/{id}/averagelaptime")
    public double getAverageLaptime(@PathVariable Long id) {
        RunnerEntity runner = runnerRepository.findById(id).orElse(null);
        if (runner != null) {
            List<LapTimeEntity> laptimes = runner.getLaptimes();
            int totalTime = 0;
            for (LapTimeEntity laptime : laptimes) {
                totalTime += laptime.getTimeSeconds();
            }
            double averageLaptime = (double) totalTime / laptimes.size();
            return averageLaptime;
        } else {
            return -1.0;
        }
    }

    @PostMapping("/{id}/changeshoename")
    public ResponseEntity<String> changeShoeName(@PathVariable Long id, @RequestBody ShoeNameRequest shoeNameRequest) {
        RunnerEntity runner = runnerRepository.findById(id).orElse(null);
        ShoeNameEntity shoeName = shoeNameRepository.findById(shoeNameRequest.shoeNameId).orElse(null);

        if (runner == null || shoeName == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("HIBA! A futó / cipő nem található!");
        }

        runner.setShoeName(shoeName);
        runnerRepository.save(runner);

        return ResponseEntity.ok("A futó cipőtípusa megváltoztatva.");
    }

    @GetMapping("/averagepace")
    public double getAveragePace() {
        List<RunnerEntity> runnerList = runnerRepository.findAll();
        if (!runnerList.isEmpty()) {
            int sumOfPace = 0;
            int numberOfPace = 0;
            for(int i = 0; i < runnerList.size(); i++){
                sumOfPace += runnerList.get(i).getPace();
                numberOfPace++;
            }
            return (double) sumOfPace / numberOfPace;
        } else {
            return -1.00;
        }
    }
    @GetMapping("/biggestshoesize")
    public String getBiggestShoeSize() {
        List<RunnerEntity> runnerList = runnerRepository.findAll();
        if (!runnerList.isEmpty()) {
            RunnerEntity biggestShoeSize = runnerList.get(0);
            for(int i = 1; i < runnerList.size(); i++){
                if (biggestShoeSize.getShoeSize() < runnerList.get(i).getShoeSize())  {
                    biggestShoeSize = runnerList.get(i);
                }
            }
            return biggestShoeSize.getRunnerName();
        } else {
            return "Hiba";
        }
    }
    @GetMapping("")
    public List<RunnerEntity> getAllRunners() {
        return runnerRepository.findAll();
    }

    @PostMapping("/{id}/addlaptime")
    public ResponseEntity addLaptime(@PathVariable Long id, @RequestBody LapTimeRequest lapTimeRequest) {
        RunnerEntity runner = runnerRepository.findById(id).orElse(null);
        if (runner != null) {
            LapTimeEntity lapTime = new LapTimeEntity();
            lapTime.setTimeSeconds(lapTimeRequest.getLapTimeSeconds());
            lapTime.setLapNumber(runner.getLaptimes().size() + 1);
            lapTime.setRunner(runner);
            lapTimeRepository.save(lapTime);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Runner with ID " + id + " not found");
        }
    }
    public static class LapTimeRequest {
        private int lapTimeSeconds;

        public int getLapTimeSeconds() {
            return lapTimeSeconds;
        }

        public void setLapTimeSeconds(int lapTimeSeconds) {
            this.lapTimeSeconds = lapTimeSeconds;
        }
    }

    public static class ShoeNameRequest{
        private long shoeNameId;

        public long getShoeNameId() {
            return shoeNameId;
        }

        public void setShoeNameId(long shoeNameId) {
            this.shoeNameId = shoeNameId;
        }
    }
}
