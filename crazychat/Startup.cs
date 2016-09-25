namespace crazychat
{
    using Microsoft.AspNetCore.Builder;
    using Microsoft.Extensions.DependencyInjection;

    public class Startup
    {

        public void Configure(IApplicationBuilder app)
        {
            app.UseMvc();
        }

        public void ConfigureServices(IServiceCollection services) 
        {
            services.AddSingleton<ICrazyChatServer, CrazyChatServer>();
            services.AddMvc();
        }
    }
}
