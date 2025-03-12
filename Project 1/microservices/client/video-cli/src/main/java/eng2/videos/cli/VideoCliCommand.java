package eng2.videos.cli;

import eng2.videos.cli.hashtags.GetHashtagCommand;
import eng2.videos.cli.hashtags.GetHashtagVideosCommand;
import eng2.videos.cli.hashtags.GetHashtagsCommand;
import eng2.videos.cli.users.CreateUserCommand;
import eng2.videos.cli.users.CreateVideoCommand;
import eng2.videos.cli.users.DeleteUserCommand;
import eng2.videos.cli.users.DeleteVideoCommand;
import eng2.videos.cli.users.GetDislikedVideosCommand;
import eng2.videos.cli.users.GetLikedVideosCommand;
import eng2.videos.cli.users.GetUploadsCommand;
import eng2.videos.cli.users.GetUserCommand;
import eng2.videos.cli.users.GetUsersCommand;
import eng2.videos.cli.users.GetViewedVideosCommand;
import eng2.videos.cli.users.UpdateUserCommand;
import eng2.videos.cli.users.UpdateVideoCommand;
import eng2.videos.cli.videos.AddDislikeCommand;
import eng2.videos.cli.videos.AddLikeCommand;
import eng2.videos.cli.videos.AddViewCommand;
import eng2.videos.cli.videos.GetDislikesCommand;
import eng2.videos.cli.videos.GetLikesCommand;
import eng2.videos.cli.videos.GetVideoCommand;
import eng2.videos.cli.videos.GetVideosCommand;
import eng2.videos.cli.videos.GetViewersCommand;
import eng2.videos.cli.videos.RemoveDislikeCommand;
import eng2.videos.cli.videos.RemoveLikeCommand;
import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "video-cli", description = "...",
        subcommands = {GetHashtagsCommand.class, GetHashtagCommand.class, GetHashtagVideosCommand.class,GetVideosCommand.class, GetVideoCommand.class, GetViewersCommand.class,
        		GetLikesCommand.class, GetDislikesCommand.class, AddViewCommand.class, AddLikeCommand.class, AddDislikeCommand.class, GetUsersCommand.class, GetUserCommand.class,
        		CreateUserCommand.class, UpdateUserCommand.class, DeleteUserCommand.class, RemoveLikeCommand.class, RemoveDislikeCommand.class, GetUploadsCommand.class,
        		GetViewedVideosCommand.class, GetLikedVideosCommand.class, GetDislikedVideosCommand.class, CreateVideoCommand.class, UpdateVideoCommand.class, DeleteVideoCommand.class},
        mixinStandardHelpOptions = true)
public class VideoCliCommand implements Runnable {

    @Option(names = {"-v", "--verbose"}, description = "...")
    boolean verbose;

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(VideoCliCommand.class, args);
    }

    public void run() {
        // business logic here
        if (verbose) {
            System.out.println("Hi!");
        }
    }
}
